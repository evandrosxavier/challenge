package br.com.fiap.challenge.application.service;

import br.com.fiap.challenge.application.port.RestauranteRepositoryPort;
import br.com.fiap.challenge.application.port.UsuarioRepositoryPort;
import br.com.fiap.challenge.interfaces.dto.request.RestauranteRequest;
import br.com.fiap.challenge.interfaces.dto.request.RestauranteUpdateRequest;
import br.com.fiap.challenge.interfaces.dto.response.RestauranteResponse;
import br.com.fiap.challenge.application.exception.BusinessException;
import br.com.fiap.challenge.interfaces.mapper.EnderecoMapper;
import br.com.fiap.challenge.interfaces.mapper.RestauranteMapper;
import br.com.fiap.challenge.domain.entities.EnderecoRestaurante;
import br.com.fiap.challenge.application.exception.ErrorCode;
import br.com.fiap.challenge.domain.entities.Restaurante;
import br.com.fiap.challenge.domain.entities.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RestauranteService {

    private final RestauranteRepositoryPort restauranteRepository;
    private final RestauranteMapper restauranteMapper;
    private final EnderecoMapper enderecoMapper;
    private final UsuarioRepositoryPort usuarioRepository;

    public RestauranteService(
        RestauranteRepositoryPort restauranteRepository,
        RestauranteMapper restauranteMapper,
        EnderecoMapper enderecoMapper,
        UsuarioRepositoryPort usuarioRepository
    ) {
        this.restauranteRepository = restauranteRepository;
        this.restauranteMapper = restauranteMapper;
        this.enderecoMapper = enderecoMapper;
        this.usuarioRepository = usuarioRepository;
    }

    public RestauranteResponse findById(Long id) {
        Restaurante restaurante = this.restauranteRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESTAURANT_NOT_FOUND, HttpStatus.NOT_FOUND));
        return restauranteMapper.toResponseDTO(restaurante);
    }

    public List<RestauranteResponse> findAll() {
        return this.restauranteRepository.findAll().stream()
                .map(restauranteMapper::toResponseDTO).toList();
    }

    @Transactional
    public RestauranteResponse save(RestauranteRequest restauranteRequest) {
        if (this.restauranteRepository.findByNomeIgnoreCase(restauranteRequest.nome()).isPresent()) {
            throw new BusinessException(ErrorCode.RESTAURANT_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }

        Usuario donoRestaurante = this.usuarioRepository.findById(restauranteRequest.idDonoRestaurante())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        Restaurante restaurante = restauranteMapper.toEntity(restauranteRequest);

        restaurante.setDonoRestaurante(donoRestaurante);

        restaurante.setDataCriacao(LocalDateTime.now());

        Restaurante restauranteSalvo = this.restauranteRepository.save(restaurante);
        return restauranteMapper.toResponseDTO(restauranteSalvo);
    }

    @Transactional
    public RestauranteResponse update(Long id, RestauranteUpdateRequest restauranteUpdateRequest) {
        Restaurante restaurante = this.restauranteRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESTAURANT_NOT_FOUND, HttpStatus.NOT_FOUND));

        if (this.restauranteRepository.findByNomeIgnoreCase(restauranteUpdateRequest.nome()).isPresent() &&
            !restaurante.getNome().equalsIgnoreCase(restauranteUpdateRequest.nome())) {
            throw new BusinessException(ErrorCode.RESTAURANT_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }

        restauranteMapper.updateEntityFromDTO(restauranteUpdateRequest, restaurante);

        if (restauranteUpdateRequest.idProprietario() != null) {
            Usuario novoProprietario = this.usuarioRepository.findById(restauranteUpdateRequest.idProprietario())
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
            restaurante.setDonoRestaurante(novoProprietario);
        }

        restaurante.getEnderecos().clear();

        if (restauranteUpdateRequest.enderecos() != null && !restauranteUpdateRequest.enderecos().isEmpty()) {
            restauranteUpdateRequest.enderecos().forEach(enderecoDTO -> {
                EnderecoRestaurante endereco = enderecoMapper.toEnderecoRestaurante(enderecoDTO);
                endereco.setRestaurante(restaurante);
                restaurante.getEnderecos().add(endereco);
            });
        }

        Restaurante restauranteSalvo = this.restauranteRepository.save(restaurante);
        return restauranteMapper.toResponseDTO(restauranteSalvo);
    }

    @Transactional
    public void delete(Long id) {
        this.restauranteRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESTAURANT_NOT_FOUND, HttpStatus.NOT_FOUND));
        this.restauranteRepository.deleteById(id);
    }

}

