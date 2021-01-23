package com.epam.logistic.exception;

public class StorageEmptyException extends Exception{
    public StorageEmptyException() {
    }

    public StorageEmptyException(String message) {
        super(message);
    }

    public StorageEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageEmptyException(Throwable cause) {
        super(cause);
    }
}
