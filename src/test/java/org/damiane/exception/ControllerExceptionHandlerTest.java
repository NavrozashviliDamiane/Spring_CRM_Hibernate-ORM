package org.damiane.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.damiane.exception.ControllerExceptionHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ControllerExceptionHandlerTest {

    @InjectMocks
    private ControllerExceptionHandler exceptionHandler;

    @Test
    void handleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Bad Request");
        ResponseEntity<ControllerExceptionHandler.ErrorResponseDTO> response = exceptionHandler.handleIllegalArgumentException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad Request", response.getBody().getMessage());
    }


    @Test
    void handleNoSuchElementException() {
        NoSuchElementException ex = new NoSuchElementException("Not Found");
        ResponseEntity<ControllerExceptionHandler.ErrorResponseDTO> response = exceptionHandler.handleNoSuchElementException(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not Found", response.getBody().getMessage());
    }

    @Test
    void handleDataIntegrityViolationException() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Conflict");
        ResponseEntity<ControllerExceptionHandler.ErrorResponseDTO> response = exceptionHandler.handleDataIntegrityViolationException(ex);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Conflict", response.getBody().getMessage());
    }

    @Test
    void handleException() {
        Exception ex = new Exception("Internal Server Error");
        ResponseEntity<ControllerExceptionHandler.ErrorResponseDTO> response = exceptionHandler.handleException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal Server Error", response.getBody().getMessage());
    }
}
