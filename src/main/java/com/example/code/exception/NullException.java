package com.example.code.exception;

public class NullException extends RuntimeException {
    public String error;
    public NullException(String error){
        this.error = error;
    }
}
