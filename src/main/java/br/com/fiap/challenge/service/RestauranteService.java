package br.com.fiap.challenge.service;

import br.com.fiap.challenge.dto.request.RestauranteRequest;
import br.com.fiap.challenge.dto.request.RestauranteUpdateRequest;
import br.com.fiap.challenge.dto.response.RestauranteResponse;
import br.com.fiap.challenge.exception.BusinessException;
import br.com.fiap.challenge.mapper.EnderecoMapper;
import br.com.fiap.challenge.mapper.RestauranteMapper;
import br.com.fiap.challenge.model.EnderecoRestaurante;
import br.com.fiap.challenge.model.ErrorCode;
import br.com.fiap.challenge.model.Restaurante;
import br.com.fiap.challenge.model.Usuario;
import br.com.fiap.challenge.repository.RestauranteRepository;
import br.com.fiap.challenge.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RestauranteService {

    private final RestauranteRepository restauranteRepository;
    private final RestauranteMapper restauranteMapper;
    private final EnderecoMapper enderecoMapper;
    private final UsuarioRepository usuarioRepository;

    public RestauranteService(RestauranteRepository restauranteRepository, RestauranteMapper restauranteMapper, EnderecoMapper enderecoMapper, UsuarioRepository usuarioRepository) {
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
        };

        Usuario donoRestaurante = this.usuarioRepository.findById(restauranteRequest.idDonoRestaurante())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

        Restaurante restaurante = restauranteMapper.toEntity(restauranteRequest);

        restaurante.setDonoRestaurante(donoRestaurante);

        EnderecoRestaurante endereco = enderecoMapper.toEnderecoRestaurante(restauranteRequest.endereco());
        endereco.setRestaurante(restaurante);
        restaurante.setEndereco(endereco);
        restaurante.setDataCriacao(LocalDateTime.now());

        Restaurante restauranteSalvo = this.restauranteRepository.save(restaurante);
        return restauranteMapper.toResponseDTO(restauranteSalvo);
    }

    @Transactional
    public RestauranteResponse update(Long id, RestauranteUpdateRequest restauranteUpdateRequest) {
        Restaurante restaurante = this.restauranteRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.RESTAURANT_NOT_FOUND, HttpStatus.NOT_FOUND));

        if (this.restauranteRepository.findByNomeIgnoreCase(restauranteUpdateRequest.nome()).isPresent()) {
                    throw new BusinessException(ErrorCode.RESTAURANT_ALREADY_EXISTS, HttpStatus.CONFLICT);
        };

        restauranteMapper.updateEntityFromDTO(restauranteUpdateRequest, restaurante);

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

