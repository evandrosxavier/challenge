package br.com.fiap.challenge.application.service;

import br.com.fiap.challenge.application.port.ItemCardapioRepositoryPort;
import br.com.fiap.challenge.application.port.RestauranteRepositoryPort;
import br.com.fiap.challenge.interfaces.dto.request.ItemCardapioRequest;
import br.com.fiap.challenge.interfaces.dto.request.ItemCardapioUpdateRequest;
import br.com.fiap.challenge.interfaces.dto.response.ItemCardapioResponse;
import br.com.fiap.challenge.application.exception.BusinessException;
import br.com.fiap.challenge.interfaces.mapper.ItemCardapioMapper;
import br.com.fiap.challenge.application.exception.ErrorCode;
import br.com.fiap.challenge.domain.ItemCardapio;
import br.com.fiap.challenge.domain.Restaurante;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemCardapioService {

    private final ItemCardapioRepositoryPort itemCardapioRepository;
    private final ItemCardapioMapper itemCardapioMapper;
    private final RestauranteRepositoryPort restauranteRepository;

    public ItemCardapioService(
        ItemCardapioRepositoryPort itemCardapioRepository,
        ItemCardapioMapper itemCardapioMapper,
        RestauranteRepositoryPort restauranteRepository
    ) {
        this.itemCardapioRepository = itemCardapioRepository;
        this.itemCardapioMapper = itemCardapioMapper;
        this.restauranteRepository = restauranteRepository;
    }

    public ItemCardapioResponse findById(Long id) {
        ItemCardapio item = this.itemCardapioRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.ITEM_CARDAPIO_NOT_FOUND, HttpStatus.NOT_FOUND));
        return this.itemCardapioMapper.toResponse(item);
    }

    public List<ItemCardapioResponse> findAll() {
        return this.itemCardapioRepository.findAll()
            .stream()
            .map(itemCardapioMapper::toResponse)
            .toList();
    }

    @Transactional
    public ItemCardapioResponse save(ItemCardapioRequest itemCardapioRequest) {
        if (this.itemCardapioRepository.findByNomeIgnoreCase(itemCardapioRequest.nome()).isPresent()) {
            throw new BusinessException(ErrorCode.ITEM_CARDAPIO_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }

        Restaurante restaurante = this.restauranteRepository.findById(itemCardapioRequest.idRestaurante())
                .orElseThrow(() -> new BusinessException(ErrorCode.RESTAURANT_NOT_FOUND, HttpStatus.NOT_FOUND));

        ItemCardapio itemCardapio = this.itemCardapioMapper.toEntity(itemCardapioRequest);
        itemCardapio.setRestaurante(restaurante);

        ItemCardapio itemCardapioSalvo = this.itemCardapioRepository.save(itemCardapio);
        return this.itemCardapioMapper.toResponse(itemCardapioSalvo);
    }

    @Transactional
    public ItemCardapioResponse update(Long id, ItemCardapioUpdateRequest itemCardapioUpdateRequest) {
        ItemCardapio itemCardapio = this.itemCardapioRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.ITEM_CARDAPIO_NOT_FOUND, HttpStatus.NOT_FOUND));
        if (this.itemCardapioRepository.findByNomeIgnoreCase(itemCardapioUpdateRequest.nome()).isPresent()) {
            throw new BusinessException(ErrorCode.ITEM_CARDAPIO_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }
        this.itemCardapioMapper.updateFromDTO(itemCardapioUpdateRequest, itemCardapio);
        ItemCardapio itemCardapioSalvo = this.itemCardapioRepository.save(itemCardapio);
        return itemCardapioMapper.toResponse(itemCardapioSalvo);
    }

    @Transactional
    public void delete(Long id) {
        this.itemCardapioRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.ITEM_CARDAPIO_NOT_FOUND, HttpStatus.NOT_FOUND));
        this.itemCardapioRepository.deleteById(id);
    }
}


