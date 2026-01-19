package br.com.fiap.challenge.dto.response;

import java.util.List;

public record ValidationErrorDTO(List<String> message, int status) {
}
