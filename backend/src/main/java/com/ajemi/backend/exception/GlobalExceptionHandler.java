package com.ajemi.backend.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ✅ 1️⃣ Validation errors (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 400,
                        "errors", errors
                ));
    }

    // ✅ 2️⃣ Business logic errors
@ExceptionHandler(ApiException.class)
public ResponseEntity<Map<String, Object>> handleApiException(ApiException ex) {
    return ResponseEntity
            .status(ex.getStatus()) // Hna ghadi i-sift l-status li khtariyti f l-Service
            .body(Map.of(
                    "timestamp", LocalDateTime.now(),
                    "status", ex.getStatus().value(),
                    "message", ex.getMessage()
            ));
}

    // ❌ 3️⃣ Any other error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 500,
                        "message", "Erreur interne du serveur"
                ));
    }
    @ExceptionHandler(org.springframework.security.authentication.BadCredentialsException.class)
        public ResponseEntity<Map<String, Object>> handleBadCredentials(org.springframework.security.authentication.BadCredentialsException ex) {
         return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED) // 401
            .body(Map.of(
                    "timestamp", LocalDateTime.now(),
                    "status", 401,
                    "message", "Identifiant ou mot de passe incorrect"
            ));
        }
        @ExceptionHandler(org.springframework.web.multipart.MaxUploadSizeExceededException.class)
        public ResponseEntity<Map<String, Object>> handleMaxUploadSize(org.springframework.web.multipart.MaxUploadSizeExceededException ex) {
        return ResponseEntity
            .status(HttpStatus.PAYLOAD_TOO_LARGE) // 413
            .body(Map.of(
                    "timestamp", LocalDateTime.now(),
                    "status", 413,
                    "message", "Le fichier est trop volumineux. La taille maximale est de 10MB."
            ));
        }
}

