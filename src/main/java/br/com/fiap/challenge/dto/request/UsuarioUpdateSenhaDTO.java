package br.com.fiap.challenge.dto.request; // Ajuste o pacote se necessário

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateSenhaDTO(

        @Schema(description = "Senha atual do usuário para verificação.", example = "minhaSenhaAntiga123")
        @NotBlank(message = "A senha atual não pode estar em branco.")
        String senhaAtual,

        @Schema(description = "Nova senha desejada para o usuário.", example = "minhaNovaSenhaSuperForte456")
        @NotBlank(message = "A nova senha não pode estar em branco.")
        @Size(min = 8, message = "A nova senha deve ter no mínimo 8 caracteres.")
        String novaSenha
) {
}
