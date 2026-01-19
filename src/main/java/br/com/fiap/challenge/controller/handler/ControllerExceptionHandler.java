package br.com.fiap.challenge.controller.handler;

import br.com.fiap.challenge.dto.response.EmailExistsResponseDTO;
import br.com.fiap.challenge.dto.response.IllegalArgumentExceptionDTO;
import br.com.fiap.challenge.dto.response.ResourceNotFoundResponseDTO;
import br.com.fiap.challenge.dto.response.ValidationErrorDTO;
import br.com.fiap.challenge.exception.EmailExistsException;
import br.com.fiap.challenge.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ResourceNotFoundResponseDTO> handlerResourceNotFoundException(ResourceNotFoundException e) {
        var status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status.value()).body(new ResourceNotFoundResponseDTO(e.getMessage(), status.value()));
    }

    @ExceptionHandler
    public ResponseEntity<EmailExistsResponseDTO> handlerEmailExists(EmailExistsException e) {
        var status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status.value()).body(new EmailExistsResponseDTO(e.getMessage(), status.value()));
    }

    @ExceptionHandler
    public ResponseEntity<IllegalArgumentExceptionDTO> handlerIllegalArgumentException(IllegalArgumentException e) {
        var status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status.value()).body(new IllegalArgumentExceptionDTO(e.getMessage(), status.value()));
    }

    @ExceptionHandler
    public ResponseEntity<ValidationErrorDTO> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var status = HttpStatus.BAD_REQUEST;
        List<String> errors = new ArrayList<>();
        for (var error : e.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        return ResponseEntity.status(status.value()).body(new ValidationErrorDTO(errors, status.value()));
    }

}


