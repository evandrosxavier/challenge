package br.com.fiap.challenge.dto.request;

import br.com.fiap.challenge.model.Endereco;

public record EnderecoRequestDTO(
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado,
        String cep
) {

//    public EnderecoRequestDTO(Endereco entidade) {
//        this(
//                entidade.getId(),
//                entidade.getLogradouro(),
//                entidade.getNumero(),
//                entidade.getComplemento(),
//                entidade.getBairro(),
//                entidade.getCidade(),
//                entidade.getEstado(),
//                entidade.getCep()
//        );
//    }
}