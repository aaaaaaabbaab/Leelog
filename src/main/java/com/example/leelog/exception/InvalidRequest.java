package com.example.leelog.exception;

import lombok.Getter;

/**
 *  정책상 status -> 400
 */
@Getter
//public class InvalidRequest extends RuntimeException{
public class InvalidRequest extends LeelogException{

    private static final String MESSAGE = "잘못된 요청입니다.";

//    public String fileName;
//    public String message;

    public InvalidRequest() {
        super(MESSAGE);
    }

//    public InvalidRequest(String fileName, String message) {
//        super(MESSAGE);
//        this.fileName = fileName;
//        this.message = message;
//    }

    public InvalidRequest(String fileName, String message) {
        super(MESSAGE);
        addValidation(fileName, message);

    }

    @Override
    public int getStatusCode(){
        return 400;
    }
}
