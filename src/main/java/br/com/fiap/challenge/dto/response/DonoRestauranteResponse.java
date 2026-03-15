package br.com.fiap.challenge.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record DonoRestauranteResponse(

        @Schema(description = "ID único do dono.", example = "1")
        Long id,

        @Schema(description = "Nome do dono do restaurante.", example = "João Silva")
        String nome,

        @Schema(description = "Email do dono.", example = "joao@example.com")
        String email

) {
}

