package com.jackob101.rekrutacja.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class StatusException extends RuntimeException {

    private final HttpStatus status;

    protected StatusException(){
        status = HttpStatus.BAD_REQUEST;
    }

    public StatusException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }


}
