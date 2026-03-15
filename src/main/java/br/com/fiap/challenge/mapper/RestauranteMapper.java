package br.com.fiap.challenge.mapper;

import br.com.fiap.challenge.dto.request.RestauranteRequest;
import br.com.fiap.challenge.dto.request.RestauranteUpdateRequest;
import br.com.fiap.challenge.dto.response.RestauranteResponse;
import br.com.fiap.challenge.model.Restaurante;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        uses = {EnderecoMapper.class}
)
public interface RestauranteMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "donoRestaurante", ignore = true)
    @Mapping(target = "itensCardapio", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(source = "endereco", target = "endereco")
    Restaurante toEntity(RestauranteRequest restauranteRequest);

    @Mapping(source = "donoRestaurante.id", target = "donoRestaurante.id")
    @Mapping(source = "donoRestaurante.nome", target = "donoRestaurante.nome")
    @Mapping(source = "donoRestaurante.email", target = "donoRestaurante.email")
    RestauranteResponse toResponseDTO(Restaurante restaurante);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "donoRestaurante", ignore = true)
    @Mapping(target = "itensCardapio", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    @Mapping(target = "endereco", ignore = true)
    void updateEntityFromDTO(RestauranteUpdateRequest restauranteUpdateRequest, @MappingTarget Restaurante restaurante);

    @AfterMapping
    default void linkEndereco(@MappingTarget Restaurante restaurante) {
        if (restaurante.getEndereco() != null) {
            restaurante.getEndereco().setRestaurante(restaurante);
        }
    }

}

