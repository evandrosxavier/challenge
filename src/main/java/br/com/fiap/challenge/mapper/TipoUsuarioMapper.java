package br.com.fiap.challenge.mapper;

import br.com.fiap.challenge.dto.request.TipoUsuarioRequestDTO;
import br.com.fiap.challenge.dto.request.TipoUsuarioUpdateDTO;
import br.com.fiap.challenge.dto.response.TipoUsuarioResponseDTO;
import br.com.fiap.challenge.model.TipoUsuario;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper (componentModel = "spring")

public interface TipoUsuarioMapper{


    TipoUsuario toEntity (TipoUsuarioRequestDTO tipoUsuarioRequest);
    TipoUsuarioResponseDTO toResponseDTO (TipoUsuario tipoUsuario);
    void updateEntityFromDTO(TipoUsuarioUpdateDTO tipoUsuarioUpdateDTO, @MappingTarget TipoUsuario tipoUsuario);



}
