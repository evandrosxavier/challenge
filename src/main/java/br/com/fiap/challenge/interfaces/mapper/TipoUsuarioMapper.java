package br.com.fiap.challenge.interfaces.mapper;

import br.com.fiap.challenge.interfaces.dto.request.TipoUsuarioRequestDTO;
import br.com.fiap.challenge.interfaces.dto.request.TipoUsuarioUpdateDTO;
import br.com.fiap.challenge.interfaces.dto.response.TipoUsuarioResponseDTO;
import br.com.fiap.challenge.domain.entities.TipoUsuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper (componentModel = "spring")

public interface TipoUsuarioMapper{

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuarios", ignore = true)
    TipoUsuario toEntity (TipoUsuarioRequestDTO tipoUsuarioRequest);
    TipoUsuarioResponseDTO toResponseDTO (TipoUsuario tipoUsuario);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuarios", ignore = true)
    void updateEntityFromDTO(TipoUsuarioUpdateDTO tipoUsuarioUpdateDTO, @MappingTarget TipoUsuario tipoUsuario);



}
