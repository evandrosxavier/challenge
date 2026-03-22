package br.com.fiap.challenge.application.service;

import br.com.fiap.challenge.application.exception.BusinessException;
import br.com.fiap.challenge.application.exception.ErrorCode;
import br.com.fiap.challenge.application.port.ItemCardapioRepositoryPort;
import br.com.fiap.challenge.application.port.RestauranteRepositoryPort;
import br.com.fiap.challenge.domain.entities.ItemCardapio;
import br.com.fiap.challenge.domain.entities.Restaurante;
import br.com.fiap.challenge.interfaces.dto.request.ItemCardapioRequest;
import br.com.fiap.challenge.interfaces.dto.request.ItemCardapioUpdateRequest;
import br.com.fiap.challenge.interfaces.dto.response.ItemCardapioResponse;
import br.com.fiap.challenge.interfaces.mapper.ItemCardapioMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ItemCardapioService")
class ItemCardapioServiceTest {

    @Mock
    private ItemCardapioRepositoryPort itemCardapioRepository;

    @Mock
    private RestauranteRepositoryPort restauranteRepository;

    @Mock
    private ItemCardapioMapper itemCardapioMapper;

    @InjectMocks
    private ItemCardapioService itemCardapioService;

    private ItemCardapio itemCardapio;
    private ItemCardapioResponse itemCardapioResponse;
    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        restaurante = new Restaurante();
        restaurante.setId(1L);

        itemCardapio = new ItemCardapio();
        itemCardapio.setId(1L);
        itemCardapio.setNome("Moqueca");
        itemCardapio.setPreco(new BigDecimal("45.50"));
        itemCardapio.setRestaurante(restaurante);

        itemCardapioResponse = mock(ItemCardapioResponse.class);
    }

    @Nested
    @DisplayName("Testes de Salvamento (Save)")
    class SaveTests {

        @Test
        @DisplayName("Deve salvar item cardapio com sucesso")
        void shouldSaveItemCardapioSuccessfully() {
            ItemCardapioRequest request = new ItemCardapioRequest(
                "Moqueca",
                "Moqueca tradicional",
                new BigDecimal("45.50"),
                false,
                "https://api.restaurante.com/fotos/moqueca.jpg",
                1L
            );

            ItemCardapio itemCardapioToSave = new ItemCardapio();
            itemCardapioToSave.setNome("Moqueca");
            itemCardapioToSave.setDescricao("Moqueca tradicional");
            itemCardapioToSave.setPreco(new BigDecimal("45.50"));

            when(itemCardapioRepository.findByNomeIgnoreCase("Moqueca")).thenReturn(Optional.empty());
            when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
            when(itemCardapioMapper.toEntity(request)).thenReturn(itemCardapioToSave);
            when(itemCardapioRepository.save(any(ItemCardapio.class))).thenReturn(itemCardapio);
            when(itemCardapioMapper.toResponse(any(ItemCardapio.class))).thenReturn(itemCardapioResponse);

            ItemCardapioResponse result = itemCardapioService.save(request);

            assertNotNull(result);
            verify(itemCardapioRepository, times(1)).findByNomeIgnoreCase("Moqueca");
            verify(restauranteRepository, times(1)).findById(1L);
            verify(itemCardapioRepository, times(1)).save(any(ItemCardapio.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando item cardapio já existe")
        void shouldThrowExceptionWhenItemCardapioAlreadyExists() {
            ItemCardapioRequest request = new ItemCardapioRequest(
                "Moqueca",
                "Moqueca tradicional",
                new BigDecimal("45.50"),
                false,
                "https://api.restaurante.com/fotos/moqueca.jpg",
                1L
            );

            when(itemCardapioRepository.findByNomeIgnoreCase("Moqueca")).thenReturn(Optional.of(itemCardapio));

            assertThrows(BusinessException.class, () -> itemCardapioService.save(request));
            verify(itemCardapioRepository, times(1)).findByNomeIgnoreCase("Moqueca");
            verify(restauranteRepository, never()).findById(any());
        }

        @Test
        @DisplayName("Deve lançar exceção quando restaurante não encontrado no salvamento")
        void shouldThrowExceptionWhenRestauranteNotFoundOnSave() {
            ItemCardapioRequest request = new ItemCardapioRequest(
                "Moqueca",
                "Moqueca tradicional",
                new BigDecimal("45.50"),
                false,
                "https://api.restaurante.com/fotos/moqueca.jpg",
                999L
            );

            when(itemCardapioRepository.findByNomeIgnoreCase("Moqueca")).thenReturn(Optional.empty());
            when(restauranteRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(BusinessException.class, () -> itemCardapioService.save(request));
            verify(itemCardapioRepository, times(1)).findByNomeIgnoreCase("Moqueca");
            verify(restauranteRepository, times(1)).findById(999L);
        }
    }

    @Nested
    @DisplayName("Testes de Atualização (Update)")
    class UpdateTests {

        @Test
        @DisplayName("Deve atualizar item cardapio com sucesso")
        void shouldUpdateItemCardapioSuccessfully() {
            ItemCardapioUpdateRequest request = new ItemCardapioUpdateRequest(
                "Moqueca Atualizada",
                "Moqueca com peixe fresco",
                new BigDecimal("50.00"),
                true,
                "https://api.restaurante.com/fotos/moqueca_nova.jpg"
            );

            when(itemCardapioRepository.findById(1L)).thenReturn(Optional.of(itemCardapio));
            when(itemCardapioRepository.findByNomeIgnoreCase("Moqueca Atualizada")).thenReturn(Optional.empty());
            when(itemCardapioRepository.save(any(ItemCardapio.class))).thenReturn(itemCardapio);
            when(itemCardapioMapper.toResponse(any(ItemCardapio.class))).thenReturn(itemCardapioResponse);

            ItemCardapioResponse result = itemCardapioService.update(1L, request);

            assertNotNull(result);
            verify(itemCardapioRepository, times(1)).findById(1L);
            verify(itemCardapioRepository, times(1)).findByNomeIgnoreCase("Moqueca Atualizada");
            verify(itemCardapioMapper, times(1)).updateFromDTO(request, itemCardapio);
            verify(itemCardapioRepository, times(1)).save(any(ItemCardapio.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando item cardapio não encontrado na atualização")
        void shouldThrowExceptionWhenItemCardapioNotFoundOnUpdate() {
            ItemCardapioUpdateRequest request = new ItemCardapioUpdateRequest(
                "Moqueca Atualizada",
                "Moqueca com peixe fresco",
                new BigDecimal("50.00"),
                true,
                "https://api.restaurante.com/fotos/moqueca_nova.jpg"
            );

            when(itemCardapioRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(BusinessException.class, () -> itemCardapioService.update(999L, request));
            verify(itemCardapioRepository, times(1)).findById(999L);
        }

        @Test
        @DisplayName("Deve lançar exceção quando novo nome já existe na atualização")
        void shouldThrowExceptionWhenNewNameAlreadyExists() {
            ItemCardapio existingItem = new ItemCardapio();
            existingItem.setId(2L);
            existingItem.setNome("Pastel");

            ItemCardapioUpdateRequest request = new ItemCardapioUpdateRequest(
                "Pastel",
                "Pastel de carne",
                new BigDecimal("20.00"),
                false,
                "https://api.restaurante.com/fotos/pastel.jpg"
            );

            when(itemCardapioRepository.findById(1L)).thenReturn(Optional.of(itemCardapio));
            when(itemCardapioRepository.findByNomeIgnoreCase("Pastel")).thenReturn(Optional.of(existingItem));

            assertThrows(BusinessException.class, () -> itemCardapioService.update(1L, request));
            verify(itemCardapioRepository, times(1)).findById(1L);
            verify(itemCardapioRepository, times(1)).findByNomeIgnoreCase("Pastel");
        }
    }

    @Nested
    @DisplayName("Testes de Deleção (Delete)")
    class DeleteTests {

        @Test
        @DisplayName("Deve deletar item cardapio com sucesso")
        void shouldDeleteItemCardapioSuccessfully() {
            when(itemCardapioRepository.findById(1L)).thenReturn(Optional.of(itemCardapio));
            doNothing().when(itemCardapioRepository).deleteById(1L);

            itemCardapioService.delete(1L);

            verify(itemCardapioRepository, times(1)).findById(1L);
            verify(itemCardapioRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("Deve lançar exceção quando deletando item cardapio que não existe")
        void shouldThrowExceptionWhenDeletingNonExistentItemCardapio() {
            when(itemCardapioRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(BusinessException.class, () -> itemCardapioService.delete(999L));
            verify(itemCardapioRepository, times(1)).findById(999L);
            verify(itemCardapioRepository, never()).deleteById(any());
        }
    }

    @Nested
    @DisplayName("Testes de Busca (Find)")
    class FindTests {

        @Test
        @DisplayName("Deve retornar item cardapio quando ID existe")
        void shouldReturnItemCardapioWhenIdExists() {
            when(itemCardapioRepository.findById(1L)).thenReturn(Optional.of(itemCardapio));
            when(itemCardapioMapper.toResponse(itemCardapio)).thenReturn(itemCardapioResponse);

            ItemCardapioResponse result = itemCardapioService.findById(1L);

            assertNotNull(result);
            verify(itemCardapioRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Deve lançar exceção quando item cardapio não encontrado")
        void shouldThrowExceptionWhenItemCardapioNotFound() {
            when(itemCardapioRepository.findById(999L))
                .thenThrow(new BusinessException(ErrorCode.ITEM_CARDAPIO_NOT_FOUND, HttpStatus.NOT_FOUND));

            assertThrows(BusinessException.class, () -> itemCardapioService.findById(999L));
        }

        @Test
        @DisplayName("Deve retornar lista de itens cardapio")
        void shouldReturnListOfItemCardapios() {
            ItemCardapio itemCardapio2 = new ItemCardapio();
            itemCardapio2.setId(2L);
            itemCardapio2.setNome("Pastel");

            List<ItemCardapio> itens = List.of(itemCardapio, itemCardapio2);
            ItemCardapioResponse response2 = mock(ItemCardapioResponse.class);

            when(itemCardapioRepository.findAll()).thenReturn(itens);
            when(itemCardapioMapper.toResponse(any())).thenReturn(itemCardapioResponse, response2);

            List<ItemCardapioResponse> result = itemCardapioService.findAll();

            assertNotNull(result);
            assertEquals(2, result.size());
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando nenhum item cardapio existe")
        void shouldReturnEmptyListWhenNoItemCardapiosExist() {
            when(itemCardapioRepository.findAll()).thenReturn(List.of());

            List<ItemCardapioResponse> result = itemCardapioService.findAll();

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }
    }

}



