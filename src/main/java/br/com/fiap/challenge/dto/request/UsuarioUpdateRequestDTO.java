package br.com.fiap.challenge.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UsuarioUpdateRequestDTO(

        @Schema(description = "Novo nome completo do usuário. Se não for fornecido, o nome atual será mantido.", example = "João da Silva Santos")
        String nome,

        @Schema(description = "Novo endereço de e-mail único do usuário. Se não for fornecido, o e-mail atual será mantido.", example = "joao.santos@example.com")
        @Email(message = "O formato do e-mail é inválido.")
        String email,

        @Schema(description = "Novo login de acesso do usuário. Se não for fornecido, o login atual será mantido.", example = "joao.santos")
        String login,

        @Schema(description = "Id do tipo de usuário.", example = "1")
        @NotNull(message = "O tipo do usuário não pode ser nulo.")
        @Valid
        Long tipoUsuario,

        @Schema(description = "Nova lista de endereços do usuário. Se fornecida, substituirá completamente a lista de endereços existente.")
        @Valid
        List<EnderecoRequestDTO> enderecos
) {
}
