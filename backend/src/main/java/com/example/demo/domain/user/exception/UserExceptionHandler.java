package com.example.demo.domain.user.exception;


import com.example.demo.domain.util.FailResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;



@ControllerAdvice
public class UserExceptionHandler {
    
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<FailResponse> emailAlreadyExistsExceptionHandler(EmailAlreadyExistsException e) {
        FailResponse response = FailResponse.of(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(MaxVerificationAttemptsExceededException.class)
    public ResponseEntity<FailResponse> maxVerificationAttemptsExceededExceptionHandler(MaxVerificationAttemptsExceededException e) {
        FailResponse response = FailResponse.of(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(PendingEmailNotFoundException.class)
    public ResponseEntity<FailResponse> pendingEmailNotFoundExceptionHandler(PendingEmailNotFoundException e) {
        FailResponse response = FailResponse.of(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(EmailVerificationCodeNotFoundException.class)
    public ResponseEntity<FailResponse> emailVerificationCodeNotFoundExceptionHandler(EmailVerificationCodeNotFoundException e) {
        FailResponse response = FailResponse.of(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(EmailVerificationCodeMismatchException.class)
    public ResponseEntity<FailResponse> emailVerificationCodeMismatchExceptionHandler(EmailVerificationCodeMismatchException e) {
        FailResponse response = FailResponse.of(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}