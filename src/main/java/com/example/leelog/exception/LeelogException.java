package com.example.leelog.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class LeelogException extends RuntimeException{

    public final Map<String, String> validation = new HashMap<>();
    public LeelogException(String message) {
        super(message);
    }

    public LeelogException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fileName, String message){
        validation.put(fileName, message);
    }
}
