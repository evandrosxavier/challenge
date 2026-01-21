package br.com.fiap.challenge.dto.request;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(

        @Schema(description = "Login de acesso do usuário.", example = "joao.silva", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O login não pode estar em branco.")
        String login,

        @Schema(description = "Senha de acesso do usuário.", example = "umaSenhaForte123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "A senha não pode estar em branco.")
        String senha
) {
}
