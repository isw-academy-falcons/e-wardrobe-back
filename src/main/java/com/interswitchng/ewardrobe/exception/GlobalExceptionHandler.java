package com.interswitchng.ewardrobe.exception;

import com.interswitchng.ewardrobe.utils.APIError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<APIError> handleException(InvalidEmailException e){
        e.printStackTrace();
        return ResponseEntity.badRequest().body(APIError.builder()
                        .status(HttpStatus.BAD_REQUEST)
                .message(e.getLocalizedMessage())
                .build());
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<APIError> handleException(UserAlreadyExistException e){
        e.printStackTrace();
        return ResponseEntity.badRequest().body(APIError.builder()
                        .status(HttpStatus.BAD_REQUEST)
                .message(e.getLocalizedMessage())
                .build());
    }

    @ExceptionHandler(PasswordMisMatchException.class)
    public ResponseEntity<APIError> handleException(PasswordMisMatchException e){
        e.printStackTrace();
        return ResponseEntity.badRequest().body(APIError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(e.getLocalizedMessage())
                .build());
    }

    @ExceptionHandler(EWardRobeException.class)
    public ResponseEntity<APIError> handleException(EWardRobeException e){
        e.printStackTrace();
        return ResponseEntity.badRequest().body(APIError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(e.getLocalizedMessage())
                .build());
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<APIError> handleUserNotFound(UserNotFoundException e){
        e.printStackTrace();
        return ResponseEntity.badRequest().body(APIError.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(e.getLocalizedMessage())
                .build());
    }


}
