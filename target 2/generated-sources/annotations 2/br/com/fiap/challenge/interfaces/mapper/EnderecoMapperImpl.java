package br.com.fiap.challenge.interfaces.mapper;

import br.com.fiap.challenge.domain.entities.EnderecoRestaurante;
import br.com.fiap.challenge.domain.entities.EnderecoUsuario;
import br.com.fiap.challenge.interfaces.dto.request.EnderecoRequestDTO;
import br.com.fiap.challenge.interfaces.dto.response.EnderecoResponseDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-24T21:20:32-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (GraalVM Community)"
)
@Component
public class EnderecoMapperImpl implements EnderecoMapper {

    @Override
    public EnderecoUsuario toEnderecoUsuario(EnderecoRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        EnderecoUsuario enderecoUsuario = new EnderecoUsuario();

        enderecoUsuario.setLogradouro( dto.logradouro() );
        enderecoUsuario.setNumero( dto.numero() );
        enderecoUsuario.setComplemento( dto.complemento() );
        enderecoUsuario.setBairro( dto.bairro() );
        enderecoUsuario.setCidade( dto.cidade() );
        enderecoUsuario.setEstado( dto.estado() );
        enderecoUsuario.setCep( dto.cep() );

        return enderecoUsuario;
    }

    @Override
    public EnderecoResponseDTO toResponseDTO(EnderecoUsuario endereco) {
        if ( endereco == null ) {
            return null;
        }

        Long id = null;
        String logradouro = null;
        String numero = null;
        String bairro = null;
        String complemento = null;
        String cep = null;
        String cidade = null;
        String estado = null;

        id = endereco.getId();
        logradouro = endereco.getLogradouro();
        numero = endereco.getNumero();
        bairro = endereco.getBairro();
        complemento = endereco.getComplemento();
        cep = endereco.getCep();
        cidade = endereco.getCidade();
        estado = endereco.getEstado();

        EnderecoResponseDTO enderecoResponseDTO = new EnderecoResponseDTO( id, logradouro, numero, bairro, complemento, cep, cidade, estado );

        return enderecoResponseDTO;
    }

    @Override
    public EnderecoRestaurante toEnderecoRestaurante(EnderecoRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        EnderecoRestaurante enderecoRestaurante = new EnderecoRestaurante();

        enderecoRestaurante.setLogradouro( dto.logradouro() );
        enderecoRestaurante.setNumero( dto.numero() );
        enderecoRestaurante.setComplemento( dto.complemento() );
        enderecoRestaurante.setBairro( dto.bairro() );
        enderecoRestaurante.setCidade( dto.cidade() );
        enderecoRestaurante.setEstado( dto.estado() );
        enderecoRestaurante.setCep( dto.cep() );

        return enderecoRestaurante;
    }

    @Override
    public EnderecoResponseDTO toResponseDTO(EnderecoRestaurante endereco) {
        if ( endereco == null ) {
            return null;
        }

        Long id = null;
        String logradouro = null;
        String numero = null;
        String bairro = null;
        String complemento = null;
        String cep = null;
        String cidade = null;
        String estado = null;

        id = endereco.getId();
        logradouro = endereco.getLogradouro();
        numero = endereco.getNumero();
        bairro = endereco.getBairro();
        complemento = endereco.getComplemento();
        cep = endereco.getCep();
        cidade = endereco.getCidade();
        estado = endereco.getEstado();

        EnderecoResponseDTO enderecoResponseDTO = new EnderecoResponseDTO( id, logradouro, numero, bairro, complemento, cep, cidade, estado );

        return enderecoResponseDTO;
    }
}
