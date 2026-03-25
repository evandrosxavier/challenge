package br.com.fiap.challenge.interfaces.mapper;

import br.com.fiap.challenge.domain.entities.ItemCardapio;
import br.com.fiap.challenge.interfaces.dto.request.ItemCardapioRequest;
import br.com.fiap.challenge.interfaces.dto.request.ItemCardapioUpdateRequest;
import br.com.fiap.challenge.interfaces.dto.response.ItemCardapioResponse;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-24T21:45:22-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (GraalVM Community)"
)
@Component
public class ItemCardapioMapperImpl implements ItemCardapioMapper {

    @Override
    public ItemCardapio toEntity(ItemCardapioRequest itemCardapioRequest) {
        if ( itemCardapioRequest == null ) {
            return null;
        }

        ItemCardapio itemCardapio = new ItemCardapio();

        itemCardapio.setNome( itemCardapioRequest.nome() );
        itemCardapio.setDescricao( itemCardapioRequest.descricao() );
        itemCardapio.setPreco( itemCardapioRequest.preco() );
        itemCardapio.setDisponivelApenasNoRestaurante( itemCardapioRequest.disponivelApenasNoRestaurante() );
        itemCardapio.setFotoUrl( itemCardapioRequest.fotoUrl() );

        return itemCardapio;
    }

    @Override
    public ItemCardapioResponse toResponse(ItemCardapio itemCardapio) {
        if ( itemCardapio == null ) {
            return null;
        }

        Long id = null;
        String nome = null;
        String descricao = null;
        BigDecimal preco = null;
        Boolean disponivelApenasNoRestaurante = null;
        String fotoUrl = null;

        id = itemCardapio.getId();
        nome = itemCardapio.getNome();
        descricao = itemCardapio.getDescricao();
        preco = itemCardapio.getPreco();
        disponivelApenasNoRestaurante = itemCardapio.getDisponivelApenasNoRestaurante();
        fotoUrl = itemCardapio.getFotoUrl();

        ItemCardapioResponse itemCardapioResponse = new ItemCardapioResponse( id, nome, descricao, preco, disponivelApenasNoRestaurante, fotoUrl );

        return itemCardapioResponse;
    }

    @Override
    public void updateFromDTO(ItemCardapioUpdateRequest updateRequest, ItemCardapio itemCardapio) {
        if ( updateRequest == null ) {
            return;
        }

        itemCardapio.setNome( updateRequest.nome() );
        itemCardapio.setDescricao( updateRequest.descricao() );
        itemCardapio.setPreco( updateRequest.preco() );
        itemCardapio.setDisponivelApenasNoRestaurante( updateRequest.disponivelApenasNoRestaurante() );
        itemCardapio.setFotoUrl( updateRequest.fotoUrl() );
    }
}
