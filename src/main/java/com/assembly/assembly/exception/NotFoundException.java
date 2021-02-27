package com.assembly.assembly.exception;

public class NotFoundException extends BadRequestException{
    public NotFoundException(String message) {
        super(message);
    }
}
