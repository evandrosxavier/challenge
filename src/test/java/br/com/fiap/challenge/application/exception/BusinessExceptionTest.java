package br.com.fiap.challenge.application.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("BusinessException")
class BusinessExceptionTest {

    @Test
    @DisplayName("Deve criar exception com ErrorCode e HttpStatus")
    void shouldCreateExceptionWithErrorCodeAndHttpStatus() {
        BusinessException exception = new BusinessException(
            ErrorCode.USER_NOT_FOUND,
            HttpStatus.NOT_FOUND
        );

        assertNotNull(exception);
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    @DisplayName("Deve retornar mensagem do ErrorCode")
    void shouldReturnErrorCodeMessage() {
        BusinessException exception = new BusinessException(
            ErrorCode.EMAIL_ALREADY_EXISTS,
            HttpStatus.BAD_REQUEST
        );

        assertEquals(
            "O email informado já está cadastrado.",
            exception.getErrorCode().getMessage()
        );
    }

    @Test
    @DisplayName("Deve retornar HttpStatus correto")
    void shouldReturnCorrectHttpStatus() {
        BusinessException exception = new BusinessException(
            ErrorCode.USER_NOT_FOUND,
            HttpStatus.NOT_FOUND
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals(404, exception.getHttpStatus().value());
    }

    @Test
    @DisplayName("Deve extender Exception")
    void shouldExtendException() {
        BusinessException exception = new BusinessException(
            ErrorCode.USER_TYPE_NOT_FOUND,
            HttpStatus.NOT_FOUND
        );

        assertTrue(exception instanceof Exception);
    }

    @Test
    @DisplayName("Deve validar diferentes status HTTP")
    void shouldValidateDifferentHttpStatus() {
        BusinessException badRequest = new BusinessException(
            ErrorCode.EMAIL_ALREADY_EXISTS,
            HttpStatus.BAD_REQUEST
        );

        BusinessException notFound = new BusinessException(
            ErrorCode.USER_NOT_FOUND,
            HttpStatus.NOT_FOUND
        );

        BusinessException conflict = new BusinessException(
            ErrorCode.RESTAURANT_ALREADY_EXISTS,
            HttpStatus.CONFLICT
        );

        assertEquals(HttpStatus.BAD_REQUEST, badRequest.getHttpStatus());
        assertEquals(HttpStatus.NOT_FOUND, notFound.getHttpStatus());
        assertEquals(HttpStatus.CONFLICT, conflict.getHttpStatus());
    }

    @Test
    @DisplayName("Deve preservar ErrorCode e HttpStatus após criação")
    void shouldPreserveErrorCodeAndHttpStatusAfterCreation() {
        ErrorCode errorCode = ErrorCode.INVALID_LOGIN_PASSWORD;
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        BusinessException exception = new BusinessException(errorCode, status);

        assertEquals(errorCode, exception.getErrorCode());
        assertEquals(status, exception.getHttpStatus());
        assertSame(errorCode, exception.getErrorCode());
        assertSame(status, exception.getHttpStatus());
    }
}

