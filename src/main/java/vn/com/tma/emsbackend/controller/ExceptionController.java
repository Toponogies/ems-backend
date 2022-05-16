package vn.com.tma.emsbackend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import vn.com.tma.emsbackend.model.dto.ErrorDTO;
import vn.com.tma.emsbackend.model.exception.*;

import java.util.Date;

@Slf4j
@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {
    @ExceptionHandler({
            DeviceNotFoundException.class,
            CredentialNotFoundException.class,
            PortNotFoundException.class,
            InterfaceNotFoundException.class
    })
    public ResponseEntity<ErrorDTO> handleNotFoundException(Exception ex, WebRequest request) {
        ErrorDTO errorDto = new ErrorDTO(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
            CredentialNameExistsException.class,
            DeviceLabelExistsException.class,
            DeviceIPExistsException.class,
            InterfaceNameUpdateForbiddenException.class,
            PortAndDeviceMismatchException.class
    })
    public ResponseEntity<ErrorDTO> handleExistingEntityException(Exception ex, WebRequest request) {
        ErrorDTO errorDto = new ErrorDTO(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorDTO errorDto = new ErrorDTO(new Date(), ex.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse(ex.getMessage()), request.getDescription(false));
        return handleExceptionInternal(ex, errorDto, headers, status, request);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ErrorDTO> handleUnsupportedOperationException(Exception ex, WebRequest request) {
        ErrorDTO errorDto = new ErrorDTO(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDto, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleUnexpectedException(Exception ex, WebRequest request) {
        log.error("Have an out of control error: ", ex);
        ErrorDTO errorDto = new ErrorDTO(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
