package br.com.fiap.challenge.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record TipoUsuarioResponseDTO(


        @Schema(description = "ID único do tipo de usuário gerado pelo sistema.", example = "1")
        Long id,

        @Schema(description = "Tipo de usuário cadastrado.", example = "Dono de Estabelecimento")
        String descricao
) {

}
