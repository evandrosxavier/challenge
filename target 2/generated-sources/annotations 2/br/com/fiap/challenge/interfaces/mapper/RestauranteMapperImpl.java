package br.com.fiap.challenge.interfaces.mapper;

import br.com.fiap.challenge.domain.entities.EnderecoRestaurante;
import br.com.fiap.challenge.domain.entities.Restaurante;
import br.com.fiap.challenge.domain.entities.Usuario;
import br.com.fiap.challenge.interfaces.dto.request.EnderecoRequestDTO;
import br.com.fiap.challenge.interfaces.dto.request.RestauranteRequest;
import br.com.fiap.challenge.interfaces.dto.request.RestauranteUpdateRequest;
import br.com.fiap.challenge.interfaces.dto.response.DonoRestauranteResponse;
import br.com.fiap.challenge.interfaces.dto.response.EnderecoResponseDTO;
import br.com.fiap.challenge.interfaces.dto.response.RestauranteResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-24T21:20:32-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (GraalVM Community)"
)
@Component
public class RestauranteMapperImpl implements RestauranteMapper {

    @Autowired
    private EnderecoMapper enderecoMapper;

    @Override
    public Restaurante toEntity(RestauranteRequest restauranteRequest) {
        if ( restauranteRequest == null ) {
            return null;
        }

        Restaurante restaurante = new Restaurante();

        restaurante.setEnderecos( enderecoRequestDTOListToEnderecoRestauranteList( restauranteRequest.enderecos() ) );
        restaurante.setNome( restauranteRequest.nome() );
        restaurante.setTipoCozinha( restauranteRequest.tipoCozinha() );
        restaurante.setHorarioFuncionamento( restauranteRequest.horarioFuncionamento() );

        linkEnderecos( restaurante );

        return restaurante;
    }

    @Override
    public RestauranteResponse toResponseDTO(Restaurante restaurante) {
        if ( restaurante == null ) {
            return null;
        }

        DonoRestauranteResponse donoRestaurante = null;
        Long id = null;
        String nome = null;
        String tipoCozinha = null;
        String horarioFuncionamento = null;
        List<EnderecoResponseDTO> enderecos = null;
        LocalDateTime dataCriacao = null;

        donoRestaurante = usuarioToDonoRestauranteResponse( restaurante.getDonoRestaurante() );
        id = restaurante.getId();
        nome = restaurante.getNome();
        tipoCozinha = restaurante.getTipoCozinha();
        horarioFuncionamento = restaurante.getHorarioFuncionamento();
        enderecos = enderecoRestauranteListToEnderecoResponseDTOList( restaurante.getEnderecos() );
        dataCriacao = restaurante.getDataCriacao();

        RestauranteResponse restauranteResponse = new RestauranteResponse( id, nome, tipoCozinha, horarioFuncionamento, donoRestaurante, enderecos, dataCriacao );

        return restauranteResponse;
    }

    @Override
    public void updateEntityFromDTO(RestauranteUpdateRequest restauranteUpdateRequest, Restaurante restaurante) {
        if ( restauranteUpdateRequest == null ) {
            return;
        }

        restaurante.setNome( restauranteUpdateRequest.nome() );
        restaurante.setTipoCozinha( restauranteUpdateRequest.tipoCozinha() );
        restaurante.setHorarioFuncionamento( restauranteUpdateRequest.horarioFuncionamento() );

        linkEnderecos( restaurante );
    }

    protected List<EnderecoRestaurante> enderecoRequestDTOListToEnderecoRestauranteList(List<EnderecoRequestDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<EnderecoRestaurante> list1 = new ArrayList<EnderecoRestaurante>( list.size() );
        for ( EnderecoRequestDTO enderecoRequestDTO : list ) {
            list1.add( enderecoMapper.toEnderecoRestaurante( enderecoRequestDTO ) );
        }

        return list1;
    }

    protected DonoRestauranteResponse usuarioToDonoRestauranteResponse(Usuario usuario) {
        if ( usuario == null ) {
            return null;
        }

        Long id = null;
        String nome = null;
        String email = null;

        id = usuario.getId();
        nome = usuario.getNome();
        email = usuario.getEmail();

        DonoRestauranteResponse donoRestauranteResponse = new DonoRestauranteResponse( id, nome, email );

        return donoRestauranteResponse;
    }

    protected List<EnderecoResponseDTO> enderecoRestauranteListToEnderecoResponseDTOList(List<EnderecoRestaurante> list) {
        if ( list == null ) {
            return null;
        }

        List<EnderecoResponseDTO> list1 = new ArrayList<EnderecoResponseDTO>( list.size() );
        for ( EnderecoRestaurante enderecoRestaurante : list ) {
            list1.add( enderecoMapper.toResponseDTO( enderecoRestaurante ) );
        }

        return list1;
    }
}
