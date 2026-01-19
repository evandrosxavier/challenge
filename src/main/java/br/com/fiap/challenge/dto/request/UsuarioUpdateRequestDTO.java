package br.com.fiap.challenge.dto.request; // Ajuste o pacote se necessário

import br.com.fiap.challenge.model.TipoUsuario; // Ajuste o pacote se necessário
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.util.List;

public record UsuarioUpdateRequestDTO(

        @Schema(description = "Novo nome completo do usuário. Se não for fornecido, o nome atual será mantido.", example = "João da Silva Santos")
        String nome,

        @Schema(description = "Novo endereço de e-mail único do usuário. Se não for fornecido, o e-mail atual será mantido.", example = "joao.santos@example.com")
        @Email(message = "O formato do e-mail é inválido.")
        String email,

        @Schema(description = "Novo login de acesso do usuário. Se não for fornecido, o login atual será mantido.", example = "joao.santos")
        String login,

        @Schema(description = "Novo tipo do usuário (ex: CLIENTE, ENTREGADOR). Se não for fornecido, o tipo atual será mantido.", example = "CLIENTE")
        TipoUsuario tipoUsuario,

        @Schema(description = "Nova lista de endereços do usuário. Se fornecida, substituirá completamente a lista de endereços existente.")
        @Valid // Se a lista for fornecida, seus itens devem ser válidos.
        List<EnderecoRequestDTO> enderecos
) {
}
