package br.com.fiap.challenge.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record ItemCardapioResponse(

        @Schema(description = "ID único do item do cardápio gerado pelo sistema.", example = "101")
        Long id,

        @Schema(description = "Nome do item do cardápio.", example = "Moqueca de Peixe")
        String nome,

        @Schema(description = "Descrição detalhada do item.", example = "Moqueca tradicional com peixe fresco e leite de coco")
        String descricao,

        @Schema(description = "Preço do item em reais.", example = "45.50")
        BigDecimal preco,

        @Schema(description = "Indica se o item está disponível apenas no restaurante (sem delivery).")
        Boolean disponivelApenasNoRestaurante,

        @Schema(description = "URL da foto do prato.", example = "https://api.restaurante.com/fotos/moqueca.jpg")
        String fotoUrl
) {
}
