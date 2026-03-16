package br.com.fiap.challenge.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {


    EMAIL_ALREADY_EXISTS("O email informado já está cadastrado."),
    LOGIN_ALREADY_EXISTS("O login informado já está em uso."),
    USER_TYPE_ALREADY_EXISTS("Tipo de usuário já cadastrado."),
    RESTAURANT_ALREADY_EXISTS("Restaurante já cadastrado."),
    ITEM_CARDAPIO_ALREADY_EXISTS("O item cardápio já está cadastrado."),


    USER_NOT_FOUND("Usuário não encontrado com o identificador informado."),
    USER_TYPE_NOT_FOUND("Tipo de usuário não encontrado com o identificador informado."),
    RESTAURANT_NOT_FOUND("Restaurante não encontrado com o identificador informado."),
    LOGIN_NOT_FOUND("O login informado não foi encontrado na base de dados."),
    RESOURCE_NOT_FOUND("O recurso solicitado não existe."),
    ITEM_CARDAPIO_NOT_FOUND("Item cardápio não encontrado com o identificador informado."),


    INVALID_PASSWORD("A senha atual informada está incorreta."),
    INVALID_LOGIN_PASSWORD("Login ou senha inválidos. Tente novamente!");


    private final String message;
}
