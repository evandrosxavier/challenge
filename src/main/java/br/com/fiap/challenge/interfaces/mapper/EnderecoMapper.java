package br.com.fiap.challenge.interfaces.mapper;

import br.com.fiap.challenge.interfaces.dto.request.EnderecoRequestDTO;
import br.com.fiap.challenge.interfaces.dto.response.EnderecoResponseDTO;
import br.com.fiap.challenge.domain.EnderecoUsuario;
import br.com.fiap.challenge.domain.EnderecoRestaurante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EnderecoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    EnderecoUsuario toEnderecoUsuario(EnderecoRequestDTO dto);

    EnderecoResponseDTO toResponseDTO(EnderecoUsuario endereco);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "restaurante", ignore = true)
    EnderecoRestaurante toEnderecoRestaurante(EnderecoRequestDTO dto);

    EnderecoResponseDTO toResponseDTO(EnderecoRestaurante endereco);
}
