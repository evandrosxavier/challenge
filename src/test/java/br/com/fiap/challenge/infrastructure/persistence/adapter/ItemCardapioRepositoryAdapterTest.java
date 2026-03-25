package br.com.fiap.challenge.infrastructure.persistence.adapter;

import br.com.fiap.challenge.domain.ItemCardapio;
import br.com.fiap.challenge.infrastructure.persistence.repository.ItemCardapioJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ItemCardapioRepositoryAdapter")
class ItemCardapioRepositoryAdapterTest {

    @Mock
    private ItemCardapioJpaRepository jpaRepository;

    private ItemCardapioRepositoryAdapter adapter;
    private ItemCardapio itemCardapio;

    @BeforeEach
    void setUp() {
        adapter = new ItemCardapioRepositoryAdapter(jpaRepository);
        itemCardapio = new ItemCardapio();
        itemCardapio.setId(1L);
        itemCardapio.setNome("Moqueca");
        itemCardapio.setDescricao("Moqueca de Peixe");
        itemCardapio.setPreco(new BigDecimal("45.50"));
    }

    @Nested
    @DisplayName("Testes do método findById")
    class FindByIdTests {

        @Test
        @DisplayName("Deve retornar ItemCardapio quando encontrado")
        void shouldReturnItemCardapioWhenFound() {
            when(jpaRepository.findById(1L)).thenReturn(Optional.of(itemCardapio));

            Optional<ItemCardapio> result = adapter.findById(1L);

            assertTrue(result.isPresent());
            assertEquals(itemCardapio.getId(), result.get().getId());
            verify(jpaRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Deve retornar vazio quando não encontrado")
        void shouldReturnEmptyWhenNotFound() {
            when(jpaRepository.findById(999L)).thenReturn(Optional.empty());

            Optional<ItemCardapio> result = adapter.findById(999L);

            assertFalse(result.isPresent());
            verify(jpaRepository, times(1)).findById(999L);
        }
    }

    @Nested
    @DisplayName("Testes do método findAll")
    class FindAllTests {

        @Test
        @DisplayName("Deve retornar lista de ItemCardapios")
        void shouldReturnListOfItemCardapios() {
            ItemCardapio item2 = new ItemCardapio();
            item2.setId(2L);
            item2.setNome("Pastel");

            when(jpaRepository.findAll()).thenReturn(List.of(itemCardapio, item2));

            List<ItemCardapio> result = adapter.findAll();

            assertEquals(2, result.size());
            verify(jpaRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há registros")
        void shouldReturnEmptyListWhenNoRecords() {
            when(jpaRepository.findAll()).thenReturn(List.of());

            List<ItemCardapio> result = adapter.findAll();

            assertTrue(result.isEmpty());
            verify(jpaRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Testes do método save")
    class SaveTests {

        @Test
        @DisplayName("Deve salvar ItemCardapio")
        void shouldSaveItemCardapio() {
            when(jpaRepository.save(itemCardapio)).thenReturn(itemCardapio);

            ItemCardapio result = adapter.save(itemCardapio);

            assertNotNull(result);
            assertEquals(itemCardapio.getId(), result.getId());
            verify(jpaRepository, times(1)).save(itemCardapio);
        }
    }

    @Nested
    @DisplayName("Testes do método deleteById")
    class DeleteByIdTests {

        @Test
        @DisplayName("Deve deletar ItemCardapio por ID")
        void shouldDeleteItemCardapioById() {
            doNothing().when(jpaRepository).deleteById(1L);

            adapter.deleteById(1L);

            verify(jpaRepository, times(1)).deleteById(1L);
        }
    }
}

