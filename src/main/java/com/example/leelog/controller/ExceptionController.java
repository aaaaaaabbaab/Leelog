package com.example.leelog.controller;

import com.example.leelog.exception.LeelogException;
import com.example.leelog.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ExceptionController {


//    @ResponseStatus(HttpStatus.OK)  //정상작동일때 Status 200번대
//    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 테스트하기위해 Status 400번대
//    @ExceptionHandler(Exception.class)
//    public void exceptionHandler(Exception e){
////        log.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$exceptionHandler error$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$", e);
//
//    }

    //    MethodArgumentNotValidException


    @ResponseStatus(HttpStatus.BAD_REQUEST)  // 테스트하기위해 Status 400번대
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e){

           ErrorResponse response = ErrorResponse.builder()
                   .code("400")
                   .message("잘못된 요청입니다")
                   .build();

           for(FieldError fieldError : e.getFieldErrors()){
               response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
           }
            return response;
    }





//    @ResponseStatus(HttpStatus.NOT_FOUND)  // Status 404      ////(LeelogException추가하면서)int statusCode = e.statusCode();를 추가하므로 필요없게됌
//    @ExceptionHandler(PostNotFound.class)
    @ExceptionHandler(LeelogException.class)
    @ResponseBody
//    public ErrorResponse postNotFound(PostNotFound e){
    public ResponseEntity<ErrorResponse> postNotFound(LeelogException e){

        int statusCode = e.getStatusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        //응답 json validation -> title : 제목에 바보를 포함할 수 없습니다.
//        if(e instanceof InvalidRequest){
//            InvalidRequest invalidRequest = (InvalidRequest) e;
//            String fileName = invalidRequest.getFileName();
//            String message = invalidRequest.getMessage();
//            body.addValidation(fileName,message);
//        }



        ResponseEntity<ErrorResponse> response =ResponseEntity.status(statusCode)       // abstact한 LeelogException추가하면서 각각 예외상황의 코드를 자동으로 들어가게하기위해
                .body(body);                                                            //abstact한 LeelogException추가하면서 각각 예외상황의 코드를 자동으로 들어가게하기위해

        return response;
    }

}
