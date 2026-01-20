package com.ajemi.backend.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1️⃣ Handli l-Custom Exception dyalk (ApiException)
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Map<String, Object>> handleApiException(ApiException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", ex.getStatus().value());
        body.put("message", ex.getMessage());
        
        return new ResponseEntity<>(body, ex.getStatus());
    }

    // 2️⃣ Handli ResponseStatusException (ila baqi kadd-khdem biha)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", ex.getStatusCode().value());
        body.put("message", ex.getReason());
        
        return new ResponseEntity<>(body, ex.getStatusCode());
    }

    // 3️⃣ Handli RuntimeException (Security, Database, etc.)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        ex.printStackTrace(); // 🚩 Darouri bach tchouf l-ghalta s7i7a f console dial IntelliJ
        
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = ex.getMessage();

        // Ila kanti aslan m-lwa7 "User not found" b7al f l-code dyalk l-qdim
        if ("User not found".equals(message)) {
            status = HttpStatus.NOT_FOUND;
        }

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("message", message);

        return new ResponseEntity<>(body, status);
    }
}