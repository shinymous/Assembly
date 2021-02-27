package com.assembly.assembly.exception;

public class InvalidOperationException extends BadRequestException {
    public InvalidOperationException(String message) {
        super(message);
    }
}
