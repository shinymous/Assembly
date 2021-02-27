package com.assembly.assembly.handler;

import com.assembly.assembly.dto.ResponseDTO;
import com.assembly.assembly.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class AssemblyExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ResponseDTO<?>> handleAllExceptions(Exception ex) {
        return new ResponseEntity<ResponseDTO<?>>(buildErrorDetails(ex, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ResponseDTO<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        StringBuilder sb = new StringBuilder();
        for(ObjectError objectError : ex.getBindingResult().getAllErrors()){
            sb.append("[");
            sb.append(objectError.getDefaultMessage());
            sb.append("] ");
        }
        return new ResponseEntity<ResponseDTO<?>>(buildErrorDetails(ex, HttpStatus.BAD_REQUEST, sb.toString()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<ResponseDTO<?>> handleBadRequestExceptions(Exception ex) {
        return new ResponseEntity<ResponseDTO<?>>(buildErrorDetails(ex, HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    private ResponseDTO<?> buildErrorDetails(Exception ex, HttpStatus httpStatus) {
        log.error(ex.getMessage(), ex);
        return ResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(httpStatus.value())
                .error(httpStatus.getReasonPhrase())
                .message(ex.getMessage())
                .build();
    }

    private ResponseDTO<?> buildErrorDetails(Exception ex, HttpStatus httpStatus, String message) {
        log.error(ex.getMessage(), ex);
        return ResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(httpStatus.value())
                .error(httpStatus.getReasonPhrase())
                .message(message)
                .build();
    }
}
