package com.example.code.exception;

public class ExistedException extends RuntimeException {
    public String error;
    public ExistedException(String error){
        this.error = error;
    }
}
