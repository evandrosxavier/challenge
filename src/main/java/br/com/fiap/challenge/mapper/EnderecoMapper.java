package br.com.fiap.challenge.mapper;

import br.com.fiap.challenge.dto.request.EnderecoRequestDTO;
import br.com.fiap.challenge.dto.response.EnderecoResponseDTO;
import br.com.fiap.challenge.model.EnderecoUsuario;
import br.com.fiap.challenge.model.EnderecoRestaurante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnderecoMapper {

    @Mapping(target = "usuario", ignore = true)
    EnderecoUsuario toEnderecoUsuario(EnderecoRequestDTO dto);

    EnderecoResponseDTO toResponseDTO(EnderecoUsuario endereco);

    @Mapping(target = "restaurante", ignore = true)
    EnderecoRestaurante toEnderecoRestaurante(EnderecoRequestDTO dto);

    EnderecoResponseDTO toResponseDTO(EnderecoRestaurante endereco);
}
