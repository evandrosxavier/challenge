package br.com.fiap.challenge.infrastructure.persistence.adapter;

import br.com.fiap.challenge.domain.entities.Usuario;
import br.com.fiap.challenge.infrastructure.persistence.repository.UsuarioJpaRepository;
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
@DisplayName("UsuarioRepositoryAdapter")
class UsuarioRepositoryAdapterTest {

    @Mock
    private UsuarioJpaRepository jpaRepository;

    private UsuarioRepositoryAdapter adapter;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        adapter = new UsuarioRepositoryAdapter(jpaRepository);
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@example.com");
        usuario.setLogin("joao.silva");
    }

    @Nested
    @DisplayName("Testes do método findById")
    class FindByIdTests {

        @Test
        @DisplayName("Deve retornar Usuario quando encontrado")
        void shouldReturnUsuarioWhenFound() {
            when(jpaRepository.findById(1L)).thenReturn(Optional.of(usuario));

            Optional<Usuario> result = adapter.findById(1L);

            assertTrue(result.isPresent());
            assertEquals(usuario.getId(), result.get().getId());
            verify(jpaRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Deve retornar vazio quando não encontrado")
        void shouldReturnEmptyWhenNotFound() {
            when(jpaRepository.findById(999L)).thenReturn(Optional.empty());

            Optional<Usuario> result = adapter.findById(999L);

            assertFalse(result.isPresent());
            verify(jpaRepository, times(1)).findById(999L);
        }
    }

    @Nested
    @DisplayName("Testes do método findAll")
    class FindAllTests {

        @Test
        @DisplayName("Deve retornar lista de Usuarios")
        void shouldReturnListOfUsuarios() {
            Usuario usuario2 = new Usuario();
            usuario2.setId(2L);
            usuario2.setNome("Maria Silva");

            when(jpaRepository.findAll()).thenReturn(List.of(usuario, usuario2));

            List<Usuario> result = adapter.findAll();

            assertEquals(2, result.size());
            verify(jpaRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há registros")
        void shouldReturnEmptyListWhenNoRecords() {
            when(jpaRepository.findAll()).thenReturn(List.of());

            List<Usuario> result = adapter.findAll();

            assertTrue(result.isEmpty());
            verify(jpaRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Testes do método save")
    class SaveTests {

        @Test
        @DisplayName("Deve salvar Usuario")
        void shouldSaveUsuario() {
            when(jpaRepository.save(usuario)).thenReturn(usuario);

            Usuario result = adapter.save(usuario);

            assertNotNull(result);
            assertEquals(usuario.getId(), result.getId());
            verify(jpaRepository, times(1)).save(usuario);
        }
    }

    @Nested
    @DisplayName("Testes do método deleteById")
    class DeleteByIdTests {

        @Test
        @DisplayName("Deve deletar Usuario por ID")
        void shouldDeleteUsuarioById() {
            doNothing().when(jpaRepository).deleteById(1L);

            adapter.deleteById(1L);

            verify(jpaRepository, times(1)).deleteById(1L);
        }
    }

    @Nested
    @DisplayName("Testes do método findByEmail")
    class FindByEmailTests {

        @Test
        @DisplayName("Deve encontrar Usuario por email ignore case")
        void shouldFindUsuarioByEmailIgnoreCase() {
            when(jpaRepository.findByEmailIgnoreCase("joao@example.com")).thenReturn(Optional.of(usuario));

            Optional<Usuario> result = adapter.findByEmailIgnoreCase("joao@example.com");

            assertTrue(result.isPresent());
            assertEquals(usuario.getEmail(), result.get().getEmail());
            verify(jpaRepository, times(1)).findByEmailIgnoreCase("joao@example.com");
        }

        @Test
        @DisplayName("Deve retornar vazio quando email não encontrado")
        void shouldReturnEmptyWhenEmailNotFound() {
            when(jpaRepository.findByEmailIgnoreCase("inexistente@example.com")).thenReturn(Optional.empty());

            Optional<Usuario> result = adapter.findByEmailIgnoreCase("inexistente@example.com");

            assertFalse(result.isPresent());
            verify(jpaRepository, times(1)).findByEmailIgnoreCase("inexistente@example.com");
        }
    }

    @Nested
    @DisplayName("Testes do método findByNomeContainingIgnoreCase")
    class FindByNomeTests {

        @Test
        @DisplayName("Deve encontrar Usuarios por nome contendo ignore case")
        void shouldFindUsuariosByNomeContaining() {
            Usuario usuario2 = new Usuario();
            usuario2.setId(2L);
            usuario2.setNome("João Ferreira");

            when(jpaRepository.findByNomeContainingIgnoreCase("joão")).thenReturn(List.of(usuario, usuario2));

            List<Usuario> result = adapter.findByNomeContainingIgnoreCase("joão");

            assertEquals(2, result.size());
            verify(jpaRepository, times(1)).findByNomeContainingIgnoreCase("joão");
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando nome não encontrado")
        void shouldReturnEmptyListWhenNomeNotFound() {
            when(jpaRepository.findByNomeContainingIgnoreCase("inexistente")).thenReturn(List.of());

            List<Usuario> result = adapter.findByNomeContainingIgnoreCase("inexistente");

            assertTrue(result.isEmpty());
            verify(jpaRepository, times(1)).findByNomeContainingIgnoreCase("inexistente");
        }
    }
}




