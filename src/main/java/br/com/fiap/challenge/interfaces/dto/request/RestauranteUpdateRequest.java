package br.com.fiap.challenge.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RestauranteUpdateRequest(

        @Schema(description = "Nome do restaurante.", example = "Pizzaria ABC Atualizada", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O nome do restaurante não pode estar em branco.")
        @Size(max = 150, message = "O nome não pode ter mais de 150 caracteres.")
        String nome,

        @Schema(description = "Tipo de cozinha oferecida.", example = "Italiana", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O tipo de cozinha não pode estar em branco.")
        @Size(max = 100, message = "O tipo de cozinha não pode ter mais de 100 caracteres.")
        String tipoCozinha,

        @Schema(description = "Horário de funcionamento do restaurante.", example = "11:00 - 23:30", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O horário de funcionamento não pode estar em branco.")
        @Size(max = 100, message = "O horário não pode ter mais de 100 caracteres.")
        String horarioFuncionamento,

        @Schema(description = "Id do proprietário.", example = "1")
        @NotNull(message = "O proprietário não pode ser vazio.")
        Long idProprietario,

        @Schema(description = "Nova lista de endereços do usuário. Se fornecida, substituirá completamente a lista de endereços existente.")
        @Valid
        List<EnderecoRequestDTO> enderecos

) {
}

