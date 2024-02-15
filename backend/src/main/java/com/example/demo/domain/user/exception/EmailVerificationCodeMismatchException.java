package com.example.demo.domain.user.exception;

public class EmailVerificationCodeMismatchException extends RuntimeException {
    public EmailVerificationCodeMismatchException(String message) {
        super(message);
    }
}