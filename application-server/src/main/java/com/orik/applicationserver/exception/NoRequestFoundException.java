package com.orik.applicationserver.exception;

public class NoRequestFoundException extends RuntimeException{
    public NoRequestFoundException(String message) {
        super(message);
    }

    public NoRequestFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoRequestFoundException(Throwable cause) {
        super(cause);
    }
}
