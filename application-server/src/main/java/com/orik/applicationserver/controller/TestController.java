package com.orik.applicationserver.controller;

import com.orik.applicationserver.DTO.RequestDTO;
import com.orik.applicationserver.DTO.StatusRequestDTO;
import com.orik.applicationserver.component.ThreadPool;
import com.orik.applicationserver.constant.RequestStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class TestController {

    private final ThreadPool threadPool;
    private final Map<Long, Future<Long>> taskMap = new ConcurrentHashMap<>();


    public TestController(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    @GetMapping("/get-from-app")
    public String getResponse(){
        return "response from application-server(8082)!";
    }

    @PostMapping("/get-result")
    public RequestDTO calculateFibonacci(@RequestBody RequestDTO requestDTO) throws ExecutionException, InterruptedException {
        int index = requestDTO.getRequest();
        Future<Long> task = threadPool.executeTask(index,requestDTO.getId());
        taskMap.put(requestDTO.getId(), task);
        System.out.println("start");
        requestDTO.setStatus(RequestStatus.IN_PROGRESS.getStatus());

        return requestDTO;
    }

    @PostMapping("/cancel-task/{taskId}")
    public String cancelTask(@PathVariable Long taskId) {
        Future<Long> task = taskMap.get(taskId);
        boolean cancel = task.cancel(true);
        return "canceled"+cancel;
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
                statusRequestDTO.setStatus(RequestStatus.IN_PROGRESS.getStatus());
            }
            return statusRequestDTO;
        }
        else if(future.isCancelled()){
            statusRequestDTO.setStatus(RequestStatus.CANCALED.getStatus());
            removeTask(id);
            return statusRequestDTO;
        }
        else{
            statusRequestDTO.setStatus(RequestStatus.DONE.getStatus());
            statusRequestDTO.setResult(future.get());
            removeTask(id);
            return statusRequestDTO;
        }
    }

    @GetMapping("/get-active")
    public int active() {
        return threadPool.getActiveThreadCount();
    }

    @GetMapping("/get-size")
    public int getSize() {
        return threadPool.getQueueSize();
    }

    private void removeTask(Long id){
        if(taskMap.containsKey(id)){
            taskMap.remove(id);
        }
    }
}
