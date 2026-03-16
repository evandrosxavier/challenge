package br.com.fiap.challenge.interfaces.mapper;


import br.com.fiap.challenge.interfaces.dto.request.ItemCardapioRequest;
import br.com.fiap.challenge.interfaces.dto.request.ItemCardapioUpdateRequest;
import br.com.fiap.challenge.interfaces.dto.response.ItemCardapioResponse;
import br.com.fiap.challenge.domain.entities.ItemCardapio;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper (componentModel = "spring")
public interface ItemCardapioMapper {

    ItemCardapio toEntity (ItemCardapioRequest itemCardapioRequest);
    ItemCardapioResponse toResponse (ItemCardapio itemCardapio);
    void updateFromDTO(ItemCardapioUpdateRequest updateRequest, @MappingTarget ItemCardapio itemCardapio);
}
