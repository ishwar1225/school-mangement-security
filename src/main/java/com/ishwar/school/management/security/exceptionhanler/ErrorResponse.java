package com.ishwar.school.management.security.exceptionhanler;

import org.springframework.http.ResponseEntity;

import static com.ishwar.school.management.security.exceptionhanler.ErrorType.INVALID_USER;
import static com.ishwar.school.management.security.exceptionhanler.ErrorType.UNABLE_TO_CREATE_UPDATE;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.ResponseEntity.badRequest;

public class ErrorResponse {

    public static ResponseEntity<?> resposeError(final Error error){
        return switch(error.getErrorType()){
            case INVALID_USER,UNABLE_TO_CREATE_UPDATE->badRequest().body( new HttpErrorResponse(error.getErrorType().name(),error.getErrorMessage().orElse((""))));
            case NO_RECORD_FOUND -> new ResponseEntity<>(error, NOT_FOUND);
            default -> new ResponseEntity(error, INTERNAL_SERVER_ERROR);
        };
    }
}
