package br.com.fiap.challenge.interfaces.dto.request;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ItemCardapioRequest(

        @Schema(description = "Nome do item do cardápio.", example = "Moqueca de Peixe", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "O nome do item não pode estar em branco.")
        @Size(max = 150, message = "O nome não pode ter mais de 150 caracteres.")
        String nome,

        @Schema(description = "Descrição detalhada do item.", example = "Moqueca tradicional com peixe fresco e leite de coco", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "A descrição do item não pode estar em branco.")
        @Size(max = 500, message = "A descrição não pode ter mais de 500 caracteres.")
        String descricao,

        @Schema(description = "Preço do item em reais.", example = "45.50", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "O preço não pode ser nulo.")
        @DecimalMin(value = "0.01", inclusive = true, message = "O preço deve ser maior que 0.")
        BigDecimal preco,

        @Schema(description = "Indica se o item está disponível apenas no restaurante (sem delivery).", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "A disponibilidade não pode ser nula.")
        Boolean disponivelApenasNoRestaurante,

        @Schema(description = "URL da foto do prato.", example = "https://api.restaurante.com/fotos/moqueca.jpg")
        @Size(max = 100, message = "A URL da foto não pode ter mais de 100 caracteres.")
        String fotoUrl,

        @Schema(description = "ID do restaurante ao qual pertence este item.", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "O ID do restaurante não pode ser nulo.")
        Long idRestaurante
) {
}
