package com.epam.logistic.exception;

public class IllegalProductException extends Exception {
    public IllegalProductException() {
    }

    public IllegalProductException(String message) {
        super(message);
    }

    public IllegalProductException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalProductException(Throwable cause) {
        super(cause);
    }
}
