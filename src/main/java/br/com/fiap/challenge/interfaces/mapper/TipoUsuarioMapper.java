package br.com.fiap.challenge.interfaces.mapper;

import br.com.fiap.challenge.interfaces.dto.request.TipoUsuarioRequestDTO;
import br.com.fiap.challenge.interfaces.dto.request.TipoUsuarioUpdateDTO;
import br.com.fiap.challenge.interfaces.dto.response.TipoUsuarioResponseDTO;
import br.com.fiap.challenge.domain.entities.TipoUsuario;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper (componentModel = "spring")

public interface TipoUsuarioMapper{


    TipoUsuario toEntity (TipoUsuarioRequestDTO tipoUsuarioRequest);
    TipoUsuarioResponseDTO toResponseDTO (TipoUsuario tipoUsuario);
    void updateEntityFromDTO(TipoUsuarioUpdateDTO tipoUsuarioUpdateDTO, @MappingTarget TipoUsuario tipoUsuario);



}
