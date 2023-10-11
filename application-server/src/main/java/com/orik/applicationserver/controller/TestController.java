package com.orik.applicationserver.controller;

import com.orik.applicationserver.DTO.RequestDTO;
import com.orik.applicationserver.DTO.ServerStatusDTO;
import com.orik.applicationserver.DTO.StatusRequestDTO;
import com.orik.applicationserver.component.ThreadPool;
import com.orik.applicationserver.constant.RequestStatus;
import com.orik.applicationserver.constant.TimeForSolve;
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
        Future<Long> task = threadPool.executeTask(index,requestDTO.getId());
        taskMap.put(requestDTO.getId(), task);
        requestDTO.setStatus(RequestStatus.IN_PROGRESS.getStatus()+"("+TimeForSolve.timeMap.get(threadPool.getIndexFromTask(requestDTO.getId()))+"sek.)");
        return requestDTO;
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
            return statusRequestDTO;
        }
        else{
            statusRequestDTO.setStatus(RequestStatus.DONE.getStatus());
            statusRequestDTO.setResult(future.get());
            statusRequestDTO.setTimeLeft(null);
            removeTask(id);

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
