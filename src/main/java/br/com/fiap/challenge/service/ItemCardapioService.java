package br.com.fiap.challenge.service;

import br.com.fiap.challenge.dto.request.ItemCardapioRequest;
import br.com.fiap.challenge.dto.request.ItemCardapioUpdateRequest;
import br.com.fiap.challenge.dto.response.ItemCardapioResponse;
import br.com.fiap.challenge.exception.BusinessException;
import br.com.fiap.challenge.mapper.ItemCardapioMapper;
import br.com.fiap.challenge.model.ErrorCode;
import br.com.fiap.challenge.model.ItemCardapio;
import br.com.fiap.challenge.model.Restaurante;
import br.com.fiap.challenge.repository.ItemCardapioRepository;
import br.com.fiap.challenge.repository.RestauranteRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemCardapioService {

    private final ItemCardapioRepository itemCardapioRepository;
    private final ItemCardapioMapper itemCardapioMapper;
    private final RestauranteRepository restauranteRepository;

    public ItemCardapioService(ItemCardapioRepository itemCardapioRepository, ItemCardapioMapper itemCardapioMapper, RestauranteRepository restauranteRepository) {
        this.itemCardapioRepository = itemCardapioRepository;
        this.itemCardapioMapper = itemCardapioMapper;
        this.restauranteRepository = restauranteRepository;
    }

    public ItemCardapioResponse findById(Long id) {
        ItemCardapio item = this.itemCardapioRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.ITEM_CARDAPIO_NOT_FOUND, HttpStatus.NOT_FOUND));
        return this.itemCardapioMapper.toResponse(item);
    }

    public List<ItemCardapioResponse> findAll() {
        return this.itemCardapioRepository.findAll().stream().map(itemCardapioMapper::toResponse).toList();
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

        ItemCardapio itemCardapio = this.itemCardapioRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.ITEM_CARDAPIO_NOT_FOUND, HttpStatus.NOT_FOUND));
        if (this.itemCardapioRepository.findByNomeIgnoreCase(itemCardapioUpdateRequest.nome()).isPresent()) {
            throw new BusinessException(ErrorCode.ITEM_CARDAPIO_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }
        this.itemCardapioMapper.updateFromDTO(itemCardapioUpdateRequest, itemCardapio);
        ItemCardapio itemCardapioSalvo = this.itemCardapioRepository.save(itemCardapio);
        return itemCardapioMapper.toResponse(itemCardapioSalvo);
    }

    @Transactional
    public void delete(Long id) {

        this.itemCardapioRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.ITEM_CARDAPIO_NOT_FOUND, HttpStatus.NOT_FOUND));
        this.itemCardapioRepository.deleteById(id);
    }
}


