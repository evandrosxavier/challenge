package br.com.fiap.challenge.mapper;

import br.com.fiap.challenge.dto.request.EnderecoRequestDTO;
import br.com.fiap.challenge.dto.response.EnderecoResponseDTO;
import br.com.fiap.challenge.model.Endereco;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EnderecoMapper {

    Endereco toEntity(EnderecoRequestDTO dto);
    EnderecoResponseDTO toResponseDTO(Endereco endereco);
}
