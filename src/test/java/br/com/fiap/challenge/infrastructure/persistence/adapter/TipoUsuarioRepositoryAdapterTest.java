package br.com.fiap.challenge.infrastructure.persistence.adapter;

import br.com.fiap.challenge.domain.TipoUsuario;
import br.com.fiap.challenge.infrastructure.persistence.repository.TipoUsuarioJpaRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TipoUsuarioRepositoryAdapter")
class TipoUsuarioRepositoryAdapterTest {

    @Mock
    private TipoUsuarioJpaRepository jpaRepository;

    private TipoUsuarioRepositoryAdapter adapter;
    private TipoUsuario tipoUsuario;

    @BeforeEach
    void setUp() {
        adapter = new TipoUsuarioRepositoryAdapter(jpaRepository);
        tipoUsuario = new TipoUsuario();
        tipoUsuario.setId(1L);
        tipoUsuario.setDescricao("USUARIO");
    }

    @Nested
    @DisplayName("Testes do método findById")
    class FindByIdTests {

        @Test
        @DisplayName("Deve retornar TipoUsuario quando encontrado")
        void shouldReturnTipoUsuarioWhenFound() {
            when(jpaRepository.findById(1L)).thenReturn(Optional.of(tipoUsuario));

            Optional<TipoUsuario> result = adapter.findById(1L);

            assertTrue(result.isPresent());
            assertEquals(tipoUsuario.getId(), result.get().getId());
            verify(jpaRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Deve retornar vazio quando não encontrado")
        void shouldReturnEmptyWhenNotFound() {
            when(jpaRepository.findById(999L)).thenReturn(Optional.empty());

            Optional<TipoUsuario> result = adapter.findById(999L);

            assertFalse(result.isPresent());
            verify(jpaRepository, times(1)).findById(999L);
        }
    }

    @Nested
    @DisplayName("Testes do método findAll")
    class FindAllTests {

        @Test
        @DisplayName("Deve retornar lista de TipoUsuario")
        void shouldReturnListOfTipoUsuario() {
            TipoUsuario tipo2 = new TipoUsuario();
            tipo2.setId(2L);
            tipo2.setDescricao("ADMIN");

            when(jpaRepository.findAll()).thenReturn(List.of(tipoUsuario, tipo2));

            List<TipoUsuario> result = adapter.findAll();

            assertEquals(2, result.size());
            verify(jpaRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há registros")
        void shouldReturnEmptyListWhenNoRecords() {
            when(jpaRepository.findAll()).thenReturn(List.of());

            List<TipoUsuario> result = adapter.findAll();

            assertTrue(result.isEmpty());
            verify(jpaRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Testes do método save")
    class SaveTests {

        @Test
        @DisplayName("Deve salvar TipoUsuario")
        void shouldSaveTipoUsuario() {
            when(jpaRepository.save(tipoUsuario)).thenReturn(tipoUsuario);

            TipoUsuario result = adapter.save(tipoUsuario);

            assertNotNull(result);
            assertEquals(tipoUsuario.getId(), result.getId());
            verify(jpaRepository, times(1)).save(tipoUsuario);
        }
    }

    @Nested
    @DisplayName("Testes do método deleteById")
    class DeleteByIdTests {

        @Test
        @DisplayName("Deve deletar TipoUsuario por ID")
        void shouldDeleteTipoUsuarioById() {
            doNothing().when(jpaRepository).deleteById(1L);

            adapter.deleteById(1L);

            verify(jpaRepository, times(1)).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("Testes do método findByDescricaoIgnoreCase")
    class FindByDescricaoIgnoreCaseTests {

        @Test
        @DisplayName("Deve encontrar TipoUsuario por descrição ignore case")
        void shouldFindTipoUsuarioByDescricaoIgnoreCase() {
            when(jpaRepository.findByDescricaoIgnoreCase("usuario")).thenReturn(Optional.of(tipoUsuario));

            Optional<TipoUsuario> result = adapter.findByDescricaoIgnoreCase("usuario");

            assertTrue(result.isPresent());
            assertEquals(tipoUsuario.getDescricao(), result.get().getDescricao());
            verify(jpaRepository, times(1)).findByDescricaoIgnoreCase("usuario");
        }

        @Test
        @DisplayName("Deve retornar vazio quando descrição não encontrada")
        void shouldReturnEmptyWhenDescricaoNotFound() {
            when(jpaRepository.findByDescricaoIgnoreCase("inexistente")).thenReturn(Optional.empty());

            Optional<TipoUsuario> result = adapter.findByDescricaoIgnoreCase("inexistente");

            assertFalse(result.isPresent());
            verify(jpaRepository, times(1)).findByDescricaoIgnoreCase("inexistente");
        }
    }
}

