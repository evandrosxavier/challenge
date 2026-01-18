package br.com.fiap.challenge.dto.response;

import br.com.fiap.challenge.model.Endereco;
import br.com.fiap.challenge.model.TipoUsuario;
import br.com.fiap.challenge.model.Usuario;

import java.util.List;

public record UsuarioResponseDTO(


        Long id,
        String nome,
        String email,
        TipoUsuario tipoUsuario,
        List<Endereco> enderecos
)

{
    public UsuarioResponseDTO(Usuario u) {
        this (
        u.getId(),
        u.getNome(),
        u.getEmail(),
        u.getTipoUsuario(),
        u.getEnderecos()
        );
    }
}
