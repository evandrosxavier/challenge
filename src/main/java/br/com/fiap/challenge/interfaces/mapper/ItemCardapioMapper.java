package br.com.fiap.challenge.interfaces.mapper;


import br.com.fiap.challenge.interfaces.dto.request.ItemCardapioRequest;
import br.com.fiap.challenge.interfaces.dto.request.ItemCardapioUpdateRequest;
import br.com.fiap.challenge.interfaces.dto.response.ItemCardapioResponse;
import br.com.fiap.challenge.domain.ItemCardapio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper (componentModel = "spring")
public interface ItemCardapioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "restaurante", ignore = true)
    ItemCardapio toEntity (ItemCardapioRequest itemCardapioRequest);
    ItemCardapioResponse toResponse (ItemCardapio itemCardapio);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "restaurante", ignore = true)
    void updateFromDTO(ItemCardapioUpdateRequest updateRequest, @MappingTarget ItemCardapio itemCardapio);
}
