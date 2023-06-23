package com.example.code.exception;

public class NotFoundException extends RuntimeException {
    public String error;
    public NotFoundException(String error){
        this.error = error;
    }
}
