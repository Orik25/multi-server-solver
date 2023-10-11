package com.orik.clientserver.constant;

public enum RequestStatus {
    IN_PROGRESS("in progress"), DONE("done"), CANCELED("canceled"), IN_QUEUE("in queue");

    RequestStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    private final String status;
}
