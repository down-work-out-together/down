package com.example.demo.global.exception;

import com.example.demo.domain.util.FailResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<FailResponse<String>> illegalAccessExceptionHandler(IllegalArgumentException e) {
        return responseEntity(e.getMessage());
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<FailResponse<String>> customExceptionHandler(CustomException e) {
        return responseEntity(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FailResponse<List<Map<String, Object>>>> handleValidationExceptions(MethodArgumentNotValidException e) {
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        List<Map<String, Object>> errorList = new ArrayList<>();

        for (FieldError error : errors) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("field", error.getField());
            errorMap.put("value", error.getRejectedValue());
            errorMap.put("message", error.getDefaultMessage());
            errorList.add(errorMap);
        }

        return ResponseEntity
                .badRequest()
                .body(FailResponse.of(errorList, "유효성 검사 실패"));
    }

    @ExceptionHandler(OAuth2AuthenticationException.class)
    public ResponseEntity<FailResponse<String>> oAuth2AuthenticationExceptionHandler(OAuth2AuthenticationException e) {
        return responseEntity(e.getMessage());
    }

    private ResponseEntity<FailResponse<String>> responseEntity(String message) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(FailResponse.of(message, message));
    }
}