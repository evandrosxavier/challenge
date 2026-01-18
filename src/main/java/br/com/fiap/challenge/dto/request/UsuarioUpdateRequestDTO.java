package br.com.fiap.challenge.dto.request;

import br.com.fiap.challenge.model.Endereco;
import br.com.fiap.challenge.model.TipoUsuario;

import java.util.List;

public record UsuarioUpdateRequestDTO(

        String nome,
        String email,
        String login,
        TipoUsuario tipoUsuario,
        List<EnderecoRequestDTO> enderecos
) {

}
