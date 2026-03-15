package br.com.fiap.challenge.mapper;


import br.com.fiap.challenge.dto.request.ItemCardapioRequest;
import br.com.fiap.challenge.dto.request.ItemCardapioUpdateRequest;
import br.com.fiap.challenge.dto.response.ItemCardapioResponse;
import br.com.fiap.challenge.model.ItemCardapio;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper (componentModel = "spring")
public interface ItemCardapioMapper {

    ItemCardapio toEntity (ItemCardapioRequest itemCardapioRequest);
    ItemCardapioResponse toResponse (ItemCardapio itemCardapio);
    void updateFromDTO(ItemCardapioUpdateRequest updateRequest, @MappingTarget ItemCardapio itemCardapio);
}
