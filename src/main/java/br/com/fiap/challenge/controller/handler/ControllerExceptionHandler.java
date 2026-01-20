package br.com.fiap.challenge.controller.handler;

import br.com.fiap.challenge.exception.EmailExistsException;
import br.com.fiap.challenge.exception.LoginExistsException;
import br.com.fiap.challenge.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ControllerExceptionHandler {

    /**
     * Handler para ResourceNotFoundException (erros 404 ).
     * Gera um ProblemDetail quando um recurso não é encontrado.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handlerResourceNotFoundException(ResourceNotFoundException e) {
        // Usando a mensagem exata da sua exceção no campo "detail"
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Recurso Não Encontrado");
        problemDetail.setType(URI.create("https://api.mydelivery.com/errors/not-found" ));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(EmailExistsException.class)
    public ResponseEntity<ProblemDetail> handlerEmailExists(EmailExistsException e) {
        // Usando a mensagem exata da sua exceção no campo "detail"
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        problemDetail.setTitle("O email informado já está cadastrado.");
        problemDetail.setType(URI.create("https://api.mydelivery.com/errors/data-conflict" ));

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handlerIllegalArgumentException(IllegalArgumentException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Senha atual inválida. Tente novamente!");
        problemDetail.setType(URI.create("https://api.mydelivery.com/errors/invalid-argument" ));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(LoginExistsException.class)
    public ResponseEntity<ProblemDetail> handlerLoginExistsException(LoginExistsException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Login existente");
        problemDetail.setType(URI.create("https://api.mydelivery.com/errors/invalid-argument" ));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
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
}
