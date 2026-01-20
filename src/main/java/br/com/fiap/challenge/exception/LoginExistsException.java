package br.com.fiap.challenge.exception;

public class LoginExistsException extends RuntimeException {

    public LoginExistsException(String message) {
        super(message);
    }
}
