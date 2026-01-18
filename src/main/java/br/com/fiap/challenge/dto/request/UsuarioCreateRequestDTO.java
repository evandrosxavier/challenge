package br.com.fiap.challenge.dto.request;

import br.com.fiap.challenge.model.Endereco;
import br.com.fiap.challenge.model.TipoUsuario;

import java.util.List;

public record UsuarioCreateRequestDTO(

        String nome,
        String email,
        String login,
        String senha,
        TipoUsuario tipoUsuario,
        List<EnderecoRequestDTO> enderecos
) {

}
