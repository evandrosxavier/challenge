package br.com.fiap.challenge.interfaces.mapper;

import br.com.fiap.challenge.domain.entities.TipoUsuario;
import br.com.fiap.challenge.interfaces.dto.request.TipoUsuarioRequestDTO;
import br.com.fiap.challenge.interfaces.dto.request.TipoUsuarioUpdateDTO;
import br.com.fiap.challenge.interfaces.dto.response.TipoUsuarioResponseDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-24T21:20:32-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (GraalVM Community)"
)
@Component
public class TipoUsuarioMapperImpl implements TipoUsuarioMapper {

    @Override
    public TipoUsuario toEntity(TipoUsuarioRequestDTO tipoUsuarioRequest) {
        if ( tipoUsuarioRequest == null ) {
            return null;
        }

        TipoUsuario tipoUsuario = new TipoUsuario();

        tipoUsuario.setDescricao( tipoUsuarioRequest.descricao() );

        return tipoUsuario;
    }

    @Override
    public TipoUsuarioResponseDTO toResponseDTO(TipoUsuario tipoUsuario) {
        if ( tipoUsuario == null ) {
            return null;
        }

        Long id = null;
        String descricao = null;

        id = tipoUsuario.getId();
        descricao = tipoUsuario.getDescricao();

        TipoUsuarioResponseDTO tipoUsuarioResponseDTO = new TipoUsuarioResponseDTO( id, descricao );

        return tipoUsuarioResponseDTO;
    }

    @Override
    public void updateEntityFromDTO(TipoUsuarioUpdateDTO tipoUsuarioUpdateDTO, TipoUsuario tipoUsuario) {
        if ( tipoUsuarioUpdateDTO == null ) {
            return;
        }

        tipoUsuario.setDescricao( tipoUsuarioUpdateDTO.descricao() );
    }
}
