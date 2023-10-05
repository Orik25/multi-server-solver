package com.orik.clientserver.exception;

import java.time.ZonedDateTime;

public class ErrorResponse {
    private int status;
    private String message;
    private ZonedDateTime time;

    public ErrorResponse() {
    }

    public ErrorResponse(int status, String message, ZonedDateTime time) {
        this.status = status;
        this.message = message;
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }
}