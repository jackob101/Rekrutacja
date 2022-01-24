package com.jackob101.rekrutacja.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ResponseBody
@ControllerAdvice
public class ApiAdvice {

    @ExceptionHandler(StatusException.class)
    public ResponseEntity<Object> handleException(StatusException ex) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", Timestamp.from(Instant.now()));
        response.put("message", ex.getMessage());
        response.put("httpStatus", ex.getStatus().value());

        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", Timestamp.from(Instant.now()));
        response.put("message", ex.getMessage());

        return ResponseEntity.internalServerError().body(response);
    }
}
