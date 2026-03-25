package br.com.fiap.challenge.infrastructure.persistence.adapter;

import br.com.fiap.challenge.domain.Restaurante;
import br.com.fiap.challenge.infrastructure.persistence.repository.RestauranteJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RestauranteRepositoryAdapter")
class RestauranteRepositoryAdapterTest {

    @Mock
    private RestauranteJpaRepository jpaRepository;

    private RestauranteRepositoryAdapter adapter;
    private Restaurante restaurante;

    @BeforeEach
    void setUp() {
        adapter = new RestauranteRepositoryAdapter(jpaRepository);
        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Pizzaria Bella");
        restaurante.setTipoCozinha("Italiana");
    }

    @Nested
    @DisplayName("Testes do método findById")
    class FindByIdTests {

        @Test
        @DisplayName("Deve retornar Restaurante quando encontrado")
        void shouldReturnRestauranteWhenFound() {
            when(jpaRepository.findById(1L)).thenReturn(Optional.of(restaurante));

            Optional<Restaurante> result = adapter.findById(1L);

            assertTrue(result.isPresent());
            assertEquals(restaurante.getId(), result.get().getId());
            verify(jpaRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Deve retornar vazio quando não encontrado")
        void shouldReturnEmptyWhenNotFound() {
            when(jpaRepository.findById(999L)).thenReturn(Optional.empty());

            Optional<Restaurante> result = adapter.findById(999L);

            assertFalse(result.isPresent());
            verify(jpaRepository, times(1)).findById(999L);
        }
    }

    @Nested
    @DisplayName("Testes do método findAll")
    class FindAllTests {

        @Test
        @DisplayName("Deve retornar lista de Restaurantes")
        void shouldReturnListOfRestaurantes() {
            Restaurante restaurante2 = new Restaurante();
            restaurante2.setId(2L);
            restaurante2.setNome("Churrascaria X");

            when(jpaRepository.findAll()).thenReturn(List.of(restaurante, restaurante2));

            List<Restaurante> result = adapter.findAll();

            assertEquals(2, result.size());
            verify(jpaRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há registros")
        void shouldReturnEmptyListWhenNoRecords() {
            when(jpaRepository.findAll()).thenReturn(List.of());

            List<Restaurante> result = adapter.findAll();

            assertTrue(result.isEmpty());
            verify(jpaRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Testes do método save")
    class SaveTests {

        @Test
        @DisplayName("Deve salvar Restaurante")
        void shouldSaveRestaurante() {
            when(jpaRepository.save(restaurante)).thenReturn(restaurante);

            Restaurante result = adapter.save(restaurante);

            assertNotNull(result);
            assertEquals(restaurante.getId(), result.getId());
            verify(jpaRepository, times(1)).save(restaurante);
        }
    }

    @Nested
    @DisplayName("Testes do método deleteById")
    class DeleteByIdTests {

        @Test
        @DisplayName("Deve deletar Restaurante por ID")
        void shouldDeleteRestauranteById() {
            doNothing().when(jpaRepository).deleteById(1L);

            adapter.deleteById(1L);

            verify(jpaRepository, times(1)).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("Testes do método findByNomeIgnoreCase")
    class FindByNomeIgnoreCaseTests {

        @Test
        @DisplayName("Deve encontrar Restaurante por nome ignore case")
        void shouldFindRestauranteByNomeIgnoreCase() {
            when(jpaRepository.findByNomeIgnoreCase("pizzaria bella")).thenReturn(Optional.of(restaurante));

            Optional<Restaurante> result = adapter.findByNomeIgnoreCase("pizzaria bella");

            assertTrue(result.isPresent());
            assertEquals(restaurante.getNome(), result.get().getNome());
            verify(jpaRepository, times(1)).findByNomeIgnoreCase("pizzaria bella");
        }

        @Test
        @DisplayName("Deve retornar vazio quando nome não encontrado")
        void shouldReturnEmptyWhenNomeNotFound() {
            when(jpaRepository.findByNomeIgnoreCase("inexistente")).thenReturn(Optional.empty());

            Optional<Restaurante> result = adapter.findByNomeIgnoreCase("inexistente");

            assertFalse(result.isPresent());
            verify(jpaRepository, times(1)).findByNomeIgnoreCase("inexistente");
        }
    }
}

