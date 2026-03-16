package br.com.fiap.challenge.interfaces.controller.handler;

import br.com.fiap.challenge.application.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;


@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ProblemDetail> handlerBusinessException(BusinessException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(e.getHttpStatus(), e.getMessage());
        problemDetail.setTitle("Erro de Negócio");
        problemDetail.setType(URI.create("https://api.mydelivery.com/errors/business-error"));
        return ResponseEntity.status(e.getHttpStatus()).body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Erro de Validação");
        problemDetail.setDetail("Um ou mais campos falharam na validação.");
        problemDetail.setType(URI.create("https://api.mydelivery.com/errors/validation-error" ));

        List<String> errors = new ArrayList<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }

        problemDetail.setProperty("validation_errors", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handlerHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Erro de Validação");
        problemDetail.setDetail("Erro ao processar a requisição. Verifique se todos os campos obrigatórios foram preenchidos corretamente.");
        problemDetail.setType(URI.create("https://api.mydelivery.com/errors/validation-error"));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }
}
