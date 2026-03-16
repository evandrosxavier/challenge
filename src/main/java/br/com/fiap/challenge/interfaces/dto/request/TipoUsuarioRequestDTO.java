package br.com.fiap.challenge.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record TipoUsuarioRequestDTO(

        @Schema(description = "Tipo de usuário a ser cadastrado.", example = "Dono de Estabelecimento", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "A descrição não pode estar em branco.")
        String descricao
) {

}
