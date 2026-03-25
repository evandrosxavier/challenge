package br.com.fiap.challenge.interfaces.mapper;

import br.com.fiap.challenge.domain.entities.EnderecoUsuario;
import br.com.fiap.challenge.domain.entities.Usuario;
import br.com.fiap.challenge.interfaces.dto.request.EnderecoRequestDTO;
import br.com.fiap.challenge.interfaces.dto.request.UsuarioCreateRequestDTO;
import br.com.fiap.challenge.interfaces.dto.request.UsuarioUpdateRequestDTO;
import br.com.fiap.challenge.interfaces.dto.response.EnderecoResponseDTO;
import br.com.fiap.challenge.interfaces.dto.response.TipoUsuarioResponseDTO;
import br.com.fiap.challenge.interfaces.dto.response.UsuarioResponseDTO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-24T21:45:22-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (GraalVM Community)"
)
@Component
public class UsuarioMapperImpl implements UsuarioMapper {

    @Autowired
    private EnderecoMapper enderecoMapper;
    @Autowired
    private TipoUsuarioMapper tipoUsuarioMapper;

    @Override
    public Usuario toEntity(UsuarioCreateRequestDTO usuarioCreateRequestDTO) {
        if ( usuarioCreateRequestDTO == null ) {
            return null;
        }

        Usuario usuario = new Usuario();

        usuario.setNome( usuarioCreateRequestDTO.nome() );
        usuario.setEmail( usuarioCreateRequestDTO.email() );
        usuario.setLogin( usuarioCreateRequestDTO.login() );
        usuario.setSenha( usuarioCreateRequestDTO.senha() );
        usuario.setEnderecos( enderecoRequestDTOListToEnderecoUsuarioList( usuarioCreateRequestDTO.enderecos() ) );

        linkEnderecos( usuario );

        return usuario;
    }

    @Override
    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        if ( usuario == null ) {
            return null;
        }

        Long id = null;
        String nome = null;
        String email = null;
        TipoUsuarioResponseDTO tipoUsuario = null;
        List<EnderecoResponseDTO> enderecos = null;

        id = usuario.getId();
        nome = usuario.getNome();
        email = usuario.getEmail();
        tipoUsuario = tipoUsuarioMapper.toResponseDTO( usuario.getTipoUsuario() );
        enderecos = enderecoUsuarioListToEnderecoResponseDTOList( usuario.getEnderecos() );

        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO( id, nome, email, tipoUsuario, enderecos );

        return usuarioResponseDTO;
    }

    @Override
    public void updateEntityFromDTO(UsuarioUpdateRequestDTO usuarioUpdateRequestDTO, Usuario usuario) {
        if ( usuarioUpdateRequestDTO == null ) {
            return;
        }

        usuario.setNome( usuarioUpdateRequestDTO.nome() );
        usuario.setEmail( usuarioUpdateRequestDTO.email() );
        usuario.setLogin( usuarioUpdateRequestDTO.login() );

        linkEnderecos( usuario );
    }

    protected List<EnderecoUsuario> enderecoRequestDTOListToEnderecoUsuarioList(List<EnderecoRequestDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<EnderecoUsuario> list1 = new ArrayList<EnderecoUsuario>( list.size() );
        for ( EnderecoRequestDTO enderecoRequestDTO : list ) {
            list1.add( enderecoMapper.toEnderecoUsuario( enderecoRequestDTO ) );
        }

        return list1;
    }

    protected List<EnderecoResponseDTO> enderecoUsuarioListToEnderecoResponseDTOList(List<EnderecoUsuario> list) {
        if ( list == null ) {
            return null;
        }

        List<EnderecoResponseDTO> list1 = new ArrayList<EnderecoResponseDTO>( list.size() );
        for ( EnderecoUsuario enderecoUsuario : list ) {
            list1.add( enderecoMapper.toResponseDTO( enderecoUsuario ) );
        }

        return list1;
    }
}
