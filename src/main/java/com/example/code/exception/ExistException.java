package com.example.code.exception;

public class ExistException extends RuntimeException {
    public String error;
    public ExistException(String error){
        this.error = error;
    }
}
