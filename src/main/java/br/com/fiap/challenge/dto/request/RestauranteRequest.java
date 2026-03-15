package br.com.fiap.challenge.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RestauranteRequest(

        @Schema(description = "Nome do restaurante.", example = "Pizzaria ABC", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O nome do restaurante não pode estar em branco.")
        @Size(max = 150, message = "O nome não pode ter mais de 150 caracteres.")
        String nome,

        @Schema(description = "Tipo de cozinha oferecida.", example = "Italiana", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O tipo de cozinha não pode estar em branco.")
        @Size(max = 100, message = "O tipo de cozinha não pode ter mais de 100 caracteres.")
        String tipoCozinha,

        @Schema(description = "Horário de funcionamento do restaurante.", example = "11:00 - 23:00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O horário de funcionamento não pode estar em branco.")
        @Size(max = 100, message = "O horário não pode ter mais de 100 caracteres.")
        String horarioFuncionamento,

        @Schema(description = "ID do dono do restaurante.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "O ID do dono não pode ser nulo.")
        Long idDonoRestaurante,

        @Schema(description = "Endereço do restaurante.", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "O endereço não pode ser nulo.")
        @Valid
        EnderecoRequestDTO endereco
) {
}


