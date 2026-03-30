package com.customerprofile.exception;

public class CustomerProfileNotFoundException extends RuntimeException {
    public CustomerProfileNotFoundException(String message) {
        super(message);
    }

    public CustomerProfileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
