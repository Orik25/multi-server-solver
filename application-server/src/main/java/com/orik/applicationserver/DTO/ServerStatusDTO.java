package com.orik.applicationserver.DTO;

public class ServerStatusDTO {

    private Integer activeThreads;
    private Integer requestInQueue;

    public ServerStatusDTO() {
    }

    public Integer getActiveThreads() {
        return activeThreads;
    }

    public void setActiveThreads(Integer activeThreads) {
        this.activeThreads = activeThreads;
    }

    public Integer getRequestInQueue() {
        return requestInQueue;
    }

    public void setRequestInQueue(Integer requestInQueue) {
        this.requestInQueue = requestInQueue;
    }
}
