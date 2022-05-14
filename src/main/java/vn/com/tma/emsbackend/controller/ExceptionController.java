package vn.com.tma.emsbackend.controller;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import vn.com.tma.emsbackend.dto.ErrorDto;
import vn.com.tma.emsbackend.exception.ResourceConstraintViolationException;
import vn.com.tma.emsbackend.exception.ResourceNotFoundException;

import java.util.Date;

@Slf4j
@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDto> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorDto errorDto = new ErrorDto(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceConstraintViolationException.class)
    public ResponseEntity<?> handleDataConstraintViolationException(Exception ex, WebRequest request) {
        ErrorDto errorDto = new ErrorDto(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDto, HttpStatus.CONFLICT);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorDto errorDto = new ErrorDto(new Date(), ex.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse(ex.getMessage()), request.getDescription(false));
        return handleExceptionInternal(ex, errorDto, headers, status, request);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnexpectedException(Exception ex, WebRequest request) {
        log.error("Have an out of control error: ", ex);
        ErrorDto errorDto = new ErrorDto(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
