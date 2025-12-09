package com.smartbill.fibonacci.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.smartbill.fibonacci.exception.InvalidJwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidJwtException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidJwt(InvalidJwtException ex) {
        Map<String, Object> body = Map.of(
                "timestamp", Instant.now(),
                "message", ex.getMessage()
        );
        return new ResponseEntity<>(body, UNAUTHORIZED);
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<Map<String, Object>> handleJwtVerificationException(JWTVerificationException ex) {
        Map<String, Object> body = Map.of(
                "timestamp", Instant.now(),
                "message", ex.getMessage()
        );
        return new ResponseEntity<>(body, UNAUTHORIZED);
    }

    @ExceptionHandler(SignatureVerificationException.class)
    public ResponseEntity<Map<String, Object>> handleSignatureVerificationException(SignatureVerificationException ex) {
        Map<String, Object> body = Map.of(
                "timestamp", Instant.now(),
                "message", ex.getMessage()
        );
        return new ResponseEntity<>(body, UNAUTHORIZED);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<Map<String, Object>> handleTokenExpiredException(TokenExpiredException ex) {
        Map<String, Object> body = Map.of(
                "timestamp", Instant.now(),
                "message", ex.getMessage()
        );
        return new ResponseEntity<>(body, UNAUTHORIZED);
    }


    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalStateException(IllegalStateException ex) {
        Map<String, Object> body = Map.of(
                "timestamp", Instant.now(),
                "message", ex.getMessage()
        );
        return new ResponseEntity<>(body, BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        Map<String, Object> body = Map.of(
                "timestamp", Instant.now(),
                "message", ex.getMessage()
        );
        return new ResponseEntity<>(body, INTERNAL_SERVER_ERROR);
    }
}
