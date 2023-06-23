package com.example.code.exception;

public class IncorrectException extends RuntimeException {
    public String error;
    public IncorrectException(String error){
        this.error = error;
    }
}
