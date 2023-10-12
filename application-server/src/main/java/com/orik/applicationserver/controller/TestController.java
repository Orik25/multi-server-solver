package com.orik.applicationserver.controller;

import com.orik.applicationserver.DTO.RequestDTO;
import com.orik.applicationserver.DTO.ServerStatusDTO;
import com.orik.applicationserver.DTO.StatusRequestDTO;
import com.orik.applicationserver.component.ThreadPool;
import com.orik.applicationserver.constant.JWTTokenGenerator;
import com.orik.applicationserver.constant.RequestStatus;
import com.orik.applicationserver.constant.ThreadPoolConstants;
import com.orik.applicationserver.constant.TimeForSolve;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class TestController {

    private final ThreadPool threadPool;
    private final Map<Long, Future<Long>> taskMap = new ConcurrentHashMap<>();
    private RestTemplate restTemplate;


    public TestController(ThreadPool threadPool,RestTemplate restTemplate) {
        this.threadPool = threadPool;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/get-result")
    public RequestDTO calculateFibonacci(@RequestBody RequestDTO requestDTO) throws ExecutionException, InterruptedException {
        int index = requestDTO.getRequest();
        checkForUpdate();
        if(checkInQueue()){
            requestDTO.setStatus(RequestStatus.IN_QUEUE.getStatus());
        }
        else{
            requestDTO.setStatus(RequestStatus.IN_PROGRESS.getStatus()+"("+TimeForSolve.timeMap.get(index)+"sek.)");
        }
        Future<Long> task = threadPool.executeTask(index,requestDTO.getId());
        taskMap.put(requestDTO.getId(), task);

        return requestDTO;
    }

    private void checkForUpdate(){
        if(threadPool.getActiveThreadCount() == ThreadPoolConstants.NUMBER_OF_THREADS-1 || threadPool.getQueueSize()==ThreadPoolConstants.CAPACITY_OF_QUEUE-1){
           update();
        }
    }

    private boolean checkInQueue(){
        return threadPool.getActiveThreadCount() == ThreadPoolConstants.NUMBER_OF_THREADS;
    }

    private void update(){
        String serverUrl = "http://localhost:8081/update";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer "+JWTTokenGenerator.generateToken(SecurityContextHolder.getContext().getAuthentication()));
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(serverUrl, HttpMethod.GET, httpEntity, String.class);
    }

    @PostMapping("/cancel-task/{taskId}")
    public StatusRequestDTO cancelTask(@PathVariable Long taskId) throws ExecutionException, InterruptedException {
        Future<Long> task = taskMap.get(taskId);
        boolean cancel = task.cancel(true);
        return getTaskStatus(taskId);
    }

    @PostMapping("/get-status/{id}")
    public StatusRequestDTO getTaskStatus(@PathVariable Long id) throws ExecutionException, InterruptedException {
        Future<Long> future = taskMap.get(id);
        StatusRequestDTO statusRequestDTO = new StatusRequestDTO();
        statusRequestDTO.setId(id);
        if(!future.isDone()){
            if(threadPool.isInQueue(id)){
                statusRequestDTO.setStatus(RequestStatus.IN_QUEUE.getStatus());
            }
            else {
                Long currentTime = (long) (System.nanoTime()/1e9);
                statusRequestDTO.setStatus(RequestStatus.IN_PROGRESS.getStatus());
                statusRequestDTO.setTimeLeft(TimeForSolve.timeMap.get(threadPool.getIndexFromTask(id))-(currentTime-threadPool.getTimeStart().get(statusRequestDTO.getId())));
            }
            return statusRequestDTO;
        }
        else if(future.isCancelled()){
            statusRequestDTO.setStatus(RequestStatus.CANCELED.getStatus());
            statusRequestDTO.setTimeLeft(null);
            removeTask(id);
            update();
            return statusRequestDTO;
        }
        else{
            statusRequestDTO.setStatus(RequestStatus.DONE.getStatus());
            statusRequestDTO.setResult(future.get());
            statusRequestDTO.setTimeLeft(null);
            removeTask(id);
            update();
            return statusRequestDTO;
        }
    }

    @GetMapping("/get-statistic")
    public ServerStatusDTO getStatistic(){
        ServerStatusDTO serverStatusDTO = new ServerStatusDTO();
        serverStatusDTO.setActiveThreads(threadPool.getActiveThreadCount());
        serverStatusDTO.setRequestInQueue(threadPool.getQueueSize());
        return serverStatusDTO;
    }

    private void removeTask(Long id){
        if(taskMap.containsKey(id)){
            taskMap.remove(id);
        }
    }
}
