package com.ajemi.backend.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}