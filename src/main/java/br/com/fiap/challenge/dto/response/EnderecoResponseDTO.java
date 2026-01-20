package br.com.fiap.challenge.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record EnderecoResponseDTO(

        @Schema(description = "ID único do endereço gerado pelo sistema.", example = "101")
        Long id,

        @Schema(description = "Logradouro do endereço (Rua, Avenida, etc).", example = "Avenida Brigadeiro Faria Lima")
        String logradouro,

        @Schema(description = "Número do endereço.", example = "1500")
        String numero,

        @Schema(description = "Bairro do endereço).", example = "Bela Vista")
        String bairro,

        @Schema(description = "Complemento do endereço (apartamento, bloco, etc).", example = "Andar 10, Cj 101")
        String complemento,

        @Schema(description = "CEP do endereço.", example = "01452002")
        String cep,

        @Schema(description = "Cidade do endereço.", example = "São Paulo")
        String cidade,

        @Schema(description = "Sigla do estado do endereço.", example = "SP")
        String estado
) {
}
