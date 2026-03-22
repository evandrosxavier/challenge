package br.com.fiap.challenge.interfaces.controller.handler;

import br.com.fiap.challenge.application.exception.BusinessException;
import br.com.fiap.challenge.application.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("ControllerExceptionHandler - Unit Tests")
class ControllerExceptionHandlerTest {

    @Autowired
    private ControllerExceptionHandler handler;

    @Nested
    @DisplayName("BusinessException Handler Tests")
    class BusinessExceptionHandlerTests {

        @Test
        @DisplayName("Deve capturar BusinessException e retornar ResponseEntity com ProblemDetail")
        void shouldHandleBusinessException() {
            var exception = new BusinessException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND);

            var response = handler.handlerBusinessException(exception);

            assertNotNull(response);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNotNull(response.getBody());
        }

        @Test
        @DisplayName("Deve retornar status correto para diferentes ErrorCodes")
        void shouldReturnCorrectStatusForDifferentErrorCodes() {
            var exceptions = new BusinessException[]{
                new BusinessException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND),
                new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS, HttpStatus.BAD_REQUEST),
                new BusinessException(ErrorCode.INVALID_LOGIN_PASSWORD, HttpStatus.BAD_REQUEST),
                new BusinessException(ErrorCode.RESTAURANT_NOT_FOUND, HttpStatus.NOT_FOUND),
                new BusinessException(ErrorCode.ITEM_CARDAPIO_NOT_FOUND, HttpStatus.NOT_FOUND)
            };

            for (var exception : exceptions) {
                var response = handler.handlerBusinessException(exception);
                assertEquals(exception.getHttpStatus(), response.getStatusCode());
            }
        }

        @Test
        @DisplayName("Deve ter titulo \"Erro de Negócio\" no ProblemDetail")
        void shouldHaveTitleInProblemDetail() {
            var exception = new BusinessException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND);

            var response = handler.handlerBusinessException(exception);

            assertNotNull(response.getBody());
            assertEquals("Erro de Negócio", response.getBody().getTitle());
        }

        @Test
        @DisplayName("Deve conter mensagem de erro no ProblemDetail")
        void shouldContainErrorMessageInProblemDetail() {
            var exception = new BusinessException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND);

            var response = handler.handlerBusinessException(exception);

            assertNotNull(response.getBody());
            assertEquals(ErrorCode.USER_NOT_FOUND.getMessage(), response.getBody().getDetail());
        }

        @Test
        @DisplayName("Deve conter tipo de erro correto no ProblemDetail")
        void shouldContainCorrectTypeInProblemDetail() {
            var exception = new BusinessException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND);

            var response = handler.handlerBusinessException(exception);

            assertNotNull(response.getBody());
            assertNotNull(response.getBody().getType());
            assertTrue(response.getBody().getType().toString().contains("business-error"));
        }

        @Test
        @DisplayName("Deve retornar 500 para erro interno")
        void shouldReturn500ForInternalError() {
            var exception = new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, HttpStatus.INTERNAL_SERVER_ERROR);

            var response = handler.handlerBusinessException(exception);

            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }

        @Test
        @DisplayName("Deve conter status code correto no ProblemDetail")
        void shouldContainCorrectStatusCodeInProblemDetail() {
            var exception = new BusinessException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND);

            var response = handler.handlerBusinessException(exception);

            assertNotNull(response.getBody());
            assertEquals(404, response.getBody().getStatus());
        }
    }

    @Nested
    @DisplayName("MethodArgumentNotValidException Handler Tests")
    class MethodArgumentNotValidExceptionHandlerTests {

        @Test
        @DisplayName("Deve capturar MethodArgumentNotValidException com um campo inválido")
        void shouldHandleMethodArgumentNotValidExceptionWithSingleField() {
            Map<String, Object> target = new HashMap<>();
            MapBindingResult bindingResult = new MapBindingResult(target, "test");
            bindingResult.addError(new FieldError("test", "email", "Email inválido"));

            var exception = new MethodArgumentNotValidException(null, bindingResult);

            var response = handler.handlerMethodArgumentNotValidException(exception);

            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
        }

        @Test
        @DisplayName("Deve retornar status 400 Bad Request")
        void shouldReturnBadRequestStatus() {
            Map<String, Object> target = new HashMap<>();
            MapBindingResult bindingResult = new MapBindingResult(target, "test");
            bindingResult.addError(new FieldError("test", "nome", "Nome é obrigatório"));

            var exception = new MethodArgumentNotValidException(null, bindingResult);

            var response = handler.handlerMethodArgumentNotValidException(exception);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

        @Test
        @DisplayName("Deve conter titulo \"Erro de Validação\" no ProblemDetail")
        void shouldHaveTitleInProblemDetail() {
            Map<String, Object> target = new HashMap<>();
            MapBindingResult bindingResult = new MapBindingResult(target, "test");
            bindingResult.addError(new FieldError("test", "email", "Email inválido"));

            var exception = new MethodArgumentNotValidException(null, bindingResult);

            var response = handler.handlerMethodArgumentNotValidException(exception);

            assertNotNull(response.getBody());
            assertEquals("Erro de Validação", response.getBody().getTitle());
        }

        @Test
        @DisplayName("Deve conter mensagem de detalhe no ProblemDetail")
        void shouldContainDetailMessageInProblemDetail() {
            Map<String, Object> target = new HashMap<>();
            MapBindingResult bindingResult = new MapBindingResult(target, "test");
            bindingResult.addError(new FieldError("test", "email", "Email inválido"));

            var exception = new MethodArgumentNotValidException(null, bindingResult);

            var response = handler.handlerMethodArgumentNotValidException(exception);

            assertNotNull(response.getBody());
            assertEquals("Um ou mais campos falharam na validação.", response.getBody().getDetail());
        }

        @Test
        @DisplayName("Deve conter tipo de erro correto no ProblemDetail")
        void shouldContainCorrectTypeInProblemDetail() {
            Map<String, Object> target = new HashMap<>();
            MapBindingResult bindingResult = new MapBindingResult(target, "test");
            bindingResult.addError(new FieldError("test", "email", "Email inválido"));

            var exception = new MethodArgumentNotValidException(null, bindingResult);

            var response = handler.handlerMethodArgumentNotValidException(exception);

            assertNotNull(response.getBody());
            assertNotNull(response.getBody().getType());
            assertTrue(response.getBody().getType().toString().contains("validation-error"));
        }

        @Test
        @DisplayName("Deve conter erros de validação no ProblemDetail")
        void shouldContainValidationErrorsInProblemDetail() {
            Map<String, Object> target = new HashMap<>();
            MapBindingResult bindingResult = new MapBindingResult(target, "test");
            bindingResult.addError(new FieldError("test", "email", "Email inválido"));
            bindingResult.addError(new FieldError("test", "nome", "Nome é obrigatório"));

            var exception = new MethodArgumentNotValidException(null, bindingResult);

            var response = handler.handlerMethodArgumentNotValidException(exception);

            assertNotNull(response.getBody());
            @SuppressWarnings("unchecked")
            List<String> errors = (List<String>) response.getBody().getProperties().get("validation_errors");
            assertNotNull(errors);
            assertEquals(2, errors.size());
        }

        @Test
        @DisplayName("Deve formatar erros de validação corretamente")
        void shouldFormatValidationErrorsCorrectly() {
            Map<String, Object> target = new HashMap<>();
            MapBindingResult bindingResult = new MapBindingResult(target, "test");
            bindingResult.addError(new FieldError("test", "email", "Email inválido"));

            var exception = new MethodArgumentNotValidException(null, bindingResult);

            var response = handler.handlerMethodArgumentNotValidException(exception);

            @SuppressWarnings("unchecked")
            List<String> errors = (List<String>) response.getBody().getProperties().get("validation_errors");
            assertNotNull(errors);
            assertTrue(errors.get(0).contains("email"));
            assertTrue(errors.get(0).contains("Email inválido"));
        }

        @Test
        @DisplayName("Deve conter status code 400 no ProblemDetail")
        void shouldContainCorrectStatusCodeInProblemDetail() {
            Map<String, Object> target = new HashMap<>();
            MapBindingResult bindingResult = new MapBindingResult(target, "test");
            bindingResult.addError(new FieldError("test", "email", "Email inválido"));

            var exception = new MethodArgumentNotValidException(null, bindingResult);

            var response = handler.handlerMethodArgumentNotValidException(exception);

            assertNotNull(response.getBody());
            assertEquals(400, response.getBody().getStatus());
        }
    }

    @Nested
    @DisplayName("HttpMessageNotReadableException Handler Tests")
    class HttpMessageNotReadableExceptionHandlerTests {

        @Test
        @DisplayName("Deve capturar HttpMessageNotReadableException")
        void shouldHandleHttpMessageNotReadableException() {
            var exception = new HttpMessageNotReadableException("Erro ao processar JSON");

            var response = handler.handlerHttpMessageNotReadableException(exception);

            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
        }

        @Test
        @DisplayName("Deve retornar status 400 Bad Request")
        void shouldReturnBadRequestStatus() {
            var exception = new HttpMessageNotReadableException("Erro ao processar JSON");

            var response = handler.handlerHttpMessageNotReadableException(exception);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

        @Test
        @DisplayName("Deve conter titulo \"Erro de Validação\" no ProblemDetail")
        void shouldHaveTitleInProblemDetail() {
            var exception = new HttpMessageNotReadableException("Erro ao processar JSON");

            var response = handler.handlerHttpMessageNotReadableException(exception);

            assertNotNull(response.getBody());
            assertEquals("Erro de Validação", response.getBody().getTitle());
        }

        @Test
        @DisplayName("Deve conter mensagem de detalhe apropriada no ProblemDetail")
        void shouldContainDetailMessageInProblemDetail() {
            var exception = new HttpMessageNotReadableException("Erro ao processar JSON");

            var response = handler.handlerHttpMessageNotReadableException(exception);

            assertNotNull(response.getBody());
            assertTrue(response.getBody().getDetail().contains("Erro ao processar a requisição"));
        }

        @Test
        @DisplayName("Deve conter tipo de erro correto no ProblemDetail")
        void shouldContainCorrectTypeInProblemDetail() {
            var exception = new HttpMessageNotReadableException("Erro ao processar JSON");

            var response = handler.handlerHttpMessageNotReadableException(exception);

            assertNotNull(response.getBody());
            assertNotNull(response.getBody().getType());
            assertTrue(response.getBody().getType().toString().contains("validation-error"));
        }

        @Test
        @DisplayName("Deve conter status code 400 no ProblemDetail")
        void shouldContainCorrectStatusCodeInProblemDetail() {
            var exception = new HttpMessageNotReadableException("Erro ao processar JSON");

            var response = handler.handlerHttpMessageNotReadableException(exception);

            assertNotNull(response.getBody());
            assertEquals(400, response.getBody().getStatus());
        }

        @Test
        @DisplayName("Deve mencionar campos obrigatórios na mensagem de erro")
        void shouldMentionRequiredFieldsInErrorMessage() {
            var exception = new HttpMessageNotReadableException("Erro ao processar JSON");

            var response = handler.handlerHttpMessageNotReadableException(exception);

            assertNotNull(response.getBody());
            assertTrue(response.getBody().getDetail().contains("campos obrigatórios"));
        }
    }
}

