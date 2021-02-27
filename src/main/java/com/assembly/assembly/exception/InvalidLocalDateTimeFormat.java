package com.assembly.assembly.exception;

public class InvalidLocalDateTimeFormat extends BadRequestException {

    public InvalidLocalDateTimeFormat(String message){
        super(message);
    }

    public InvalidLocalDateTimeFormat(){
        super("");
    }
}
