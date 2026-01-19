package br.com.fiap.challenge.dto.request;

import br.com.fiap.challenge.model.Endereco;
import br.com.fiap.challenge.model.TipoUsuario;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioCreateRequestDTO(

        @Schema(description = "Nome completo do usuário.", example = "João da Silva")
        @NotBlank(message = "O nome do usuário não pode estar em branco.")
        String nome,

        @Schema(description = "Endereço de e-mail único do usuário.", example = "joao.silva@example.com")
        @NotBlank(message = "O e-mail não pode estar em branco.")
        @Email(message = "O formato do e-mail é inválido.")
        String email,

        @Schema(description = "Login de acesso do usuário.", example = "joao.silva")
        @NotBlank(message = "O login não pode estar em branco.")
        String login,

        @Schema(description = "Senha de acesso do usuário.", example = "umaSenhaForte123")
        @NotBlank(message = "A senha não pode estar em branco.")
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres.")
        String senha,

        @Schema(description = "Tipo do usuário (ex: CLIENTE, ENTREGADOR).", example = "CLIENTE")
        @NotNull(message = "O tipo do usuário não pode ser nulo.")
        TipoUsuario tipoUsuario,

        @Schema(description = "Lista de endereços do usuário.")
        @NotNull(message = "A lista de endereços não pode ser nula.")
        @Size(min = 1, message = "O usuário deve ter pelo menos um endereço.")
        @Valid
        List<EnderecoRequestDTO> enderecos
) {
}

