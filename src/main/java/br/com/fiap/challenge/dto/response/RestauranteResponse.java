package br.com.fiap.challenge.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record RestauranteResponse(

        @Schema(description = "ID único do restaurante.", example = "1")
        Long id,

        @Schema(description = "Nome do restaurante.", example = "Pizzaria ABC")
        String nome,

        @Schema(description = "Tipo de cozinha.", example = "Italiana")
        String tipoCozinha,

        @Schema(description = "Horário de funcionamento.", example = "11:00 - 23:00")
        String horarioFuncionamento,

        @Schema(description = "Dono do restaurante.")
        DonoRestauranteResponse donoRestaurante,

        @Schema(description = "Endereço do restaurante.")
        EnderecoResponseDTO endereco,

        @Schema(description = "Data de criação.", example = "2026-03-15T10:30:00")
        LocalDateTime dataCriacao
) {
}


