package com.epam.logistic.exception;

public class StorageFullException extends Exception{
    public StorageFullException() {
    }

    public StorageFullException(String message) {
        super(message);
    }

    public StorageFullException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageFullException(Throwable cause) {
        super(cause);
    }
}
