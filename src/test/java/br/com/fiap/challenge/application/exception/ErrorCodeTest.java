package br.com.fiap.challenge.application.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ErrorCode")
class ErrorCodeTest {

    @Test
    @DisplayName("Deve ter mensagem para EMAIL_ALREADY_EXISTS")
    void shouldHaveMessageForEmailAlreadyExists() {
        assertEquals("O email informado já está cadastrado.", ErrorCode.EMAIL_ALREADY_EXISTS.getMessage());
    }

    @Test
    @DisplayName("Deve ter mensagem para LOGIN_ALREADY_EXISTS")
    void shouldHaveMessageForLoginAlreadyExists() {
        assertEquals("O login informado já está em uso.", ErrorCode.LOGIN_ALREADY_EXISTS.getMessage());
    }

    @Test
    @DisplayName("Deve ter mensagem para USER_TYPE_ALREADY_EXISTS")
    void shouldHaveMessageForUserTypeAlreadyExists() {
        assertEquals("Tipo de usuário já cadastrado.", ErrorCode.USER_TYPE_ALREADY_EXISTS.getMessage());
    }

    @Test
    @DisplayName("Deve ter mensagem para USER_NOT_FOUND")
    void shouldHaveMessageForUserNotFound() {
        assertEquals("Usuário não encontrado com o identificador informado.", ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("Deve ter mensagem para USER_TYPE_NOT_FOUND")
    void shouldHaveMessageForUserTypeNotFound() {
        assertEquals("Tipo de usuário não encontrado com o identificador informado.", ErrorCode.USER_TYPE_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("Deve ter mensagem para RESTAURANT_NOT_FOUND")
    void shouldHaveMessageForRestauranteNotFound() {
        assertEquals("Restaurante não encontrado com o identificador informado.", ErrorCode.RESTAURANT_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("Deve ter mensagem para INVALID_LOGIN_PASSWORD")
    void shouldHaveMessageForInvalidLoginPassword() {
        assertEquals("Login ou senha inválidos. Tente novamente!", ErrorCode.INVALID_LOGIN_PASSWORD.getMessage());
    }

    @Test
    @DisplayName("Deve ter mensagem para INVALID_PASSWORD")
    void shouldHaveMessageForInvalidPassword() {
        assertEquals("A senha atual informada está incorreta.", ErrorCode.INVALID_PASSWORD.getMessage());
    }

    @Test
    @DisplayName("Deve ter mensagem para ITEM_CARDAPIO_NOT_FOUND")
    void shouldHaveMessageForItemCardapioNotFound() {
        assertEquals("Item cardápio não encontrado com o identificador informado.", ErrorCode.ITEM_CARDAPIO_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("Deve ter mensagem para RESTAURANT_ALREADY_EXISTS")
    void shouldHaveMessageForRestauranteAlreadyExists() {
        assertEquals("Restaurante já cadastrado.", ErrorCode.RESTAURANT_ALREADY_EXISTS.getMessage());
    }

    @Test
    @DisplayName("Deve ter mensagem para ITEM_CARDAPIO_ALREADY_EXISTS")
    void shouldHaveMessageForItemCardapioAlreadyExists() {
        assertEquals("O item cardápio já está cadastrado.", ErrorCode.ITEM_CARDAPIO_ALREADY_EXISTS.getMessage());
    }

    @Test
    @DisplayName("Deve ter todas as mensagens não nulas")
    void shouldHaveAllMessagesNotNull() {
        for (ErrorCode errorCode : ErrorCode.values()) {
            assertNotNull(errorCode.getMessage());
            assertFalse(errorCode.getMessage().isEmpty());
        }
    }

    @Test
    @DisplayName("Deve ter mensagem para LOGIN_NOT_FOUND")
    void shouldHaveMessageForLoginNotFound() {
        assertEquals("O login informado não foi encontrado na base de dados.", ErrorCode.LOGIN_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("Deve ter mensagem para RESOURCE_NOT_FOUND")
    void shouldHaveMessageForResourceNotFound() {
        assertEquals("O recurso solicitado não existe.", ErrorCode.RESOURCE_NOT_FOUND.getMessage());
    }
}

