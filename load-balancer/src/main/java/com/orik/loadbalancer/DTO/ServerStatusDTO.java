package com.orik.loadbalancer.DTO;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerStatusDTO that = (ServerStatusDTO) o;
        return Objects.equals(activeThreads, that.activeThreads) && Objects.equals(requestInQueue, that.requestInQueue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activeThreads, requestInQueue);
    }
}
