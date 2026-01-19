package br.com.fiap.challenge.dto.response;

import br.com.fiap.challenge.model.Endereco;
import br.com.fiap.challenge.model.TipoUsuario;
import br.com.fiap.challenge.model.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record UsuarioResponseDTO(

        @Schema(description = "ID único do usuário gerado pelo sistema.", example = "1")
        Long id,

        @Schema(description = "Nome completo do usuário.", example = "Maria da Silva")
        String nome,

        @Schema(description = "Endereço de e-mail do usuário.", example = "maria.silva@example.com")
        String email,

        @Schema(description = "Tipo do usuário no sistema.", example = "CLIENTE")
        TipoUsuario tipoUsuario,

        @Schema(description = "Lista de endereços associados ao usuário.")
        List<EnderecoResponseDTO> enderecos
) {

}

