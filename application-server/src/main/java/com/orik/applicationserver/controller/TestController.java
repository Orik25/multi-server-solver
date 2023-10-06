package com.orik.applicationserver.controller;

import com.orik.applicationserver.component.ThreadPool;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class TestController {

    private final ThreadPool threadPool;

    public TestController(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    @GetMapping("/get-response/{body}")
    public String getResponse(@PathVariable String body){
        return "response from application-server("+body+")!";
    }

    @GetMapping("/get-result/{index}")
    public Long calculateFibonacci(@PathVariable int index) throws ExecutionException, InterruptedException {
        return threadPool.executeTask(index);
    }

    @GetMapping("/get-active")
    public int calculateFibonacci() {
        return threadPool.getActiveThreadCount();
    }
}
