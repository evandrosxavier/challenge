package br.com.fiap.challenge.application.service;

import br.com.fiap.challenge.application.exception.BusinessException;
import br.com.fiap.challenge.application.exception.ErrorCode;
import br.com.fiap.challenge.application.port.TipoUsuarioRepositoryPort;
import br.com.fiap.challenge.domain.entities.TipoUsuario;
import br.com.fiap.challenge.interfaces.dto.request.TipoUsuarioRequestDTO;
import br.com.fiap.challenge.interfaces.dto.request.TipoUsuarioUpdateDTO;
import br.com.fiap.challenge.interfaces.dto.response.TipoUsuarioResponseDTO;
import br.com.fiap.challenge.interfaces.mapper.TipoUsuarioMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TipoUsuarioService")
class TipoUsuarioServiceTest {

    @Mock
    private TipoUsuarioRepositoryPort tipoUsuarioRepository;

    @Mock
    private TipoUsuarioMapper tipoUsuarioMapper;

    @InjectMocks
    private TipoUsuarioService tipoUsuarioService;

    private TipoUsuario tipoUsuario;
    private TipoUsuarioResponseDTO tipoUsuarioResponse;

    @BeforeEach
    void setUp() {
        tipoUsuario = new TipoUsuario(1L, "Cliente", null);
        tipoUsuarioResponse = mock(TipoUsuarioResponseDTO.class);
    }

    @Nested
    @DisplayName("Testes de Busca (Find)")
    class FindTests {

        @Test
        @DisplayName("Deve retornar tipo de usuario quando ID existe")
        void shouldReturnTipoUsuarioWhenIdExists() {
            when(tipoUsuarioRepository.findById(1L)).thenReturn(Optional.of(tipoUsuario));
            when(tipoUsuarioMapper.toResponseDTO(tipoUsuario)).thenReturn(tipoUsuarioResponse);

            TipoUsuarioResponseDTO result = tipoUsuarioService.findById(1L);

            assertNotNull(result);
            verify(tipoUsuarioRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Deve lançar exceção quando tipo de usuario não encontrado")
        void shouldThrowExceptionWhenTipoUsuarioNotFound() {
            when(tipoUsuarioRepository.findById(999L))
                .thenReturn(Optional.empty());

            assertThrows(BusinessException.class, () -> tipoUsuarioService.findById(999L));
            verify(tipoUsuarioRepository, times(1)).findById(999L);
        }

        @Test
        @DisplayName("Deve retornar lista de tipos de usuario")
        void shouldReturnListOfTipoUsuarios() {
            TipoUsuario tipoUsuario2 = new TipoUsuario(2L, "ADMIN", null);
            List<TipoUsuario> tipoUsuarios = List.of(tipoUsuario, tipoUsuario2);

            when(tipoUsuarioRepository.findAll()).thenReturn(tipoUsuarios);
            when(tipoUsuarioMapper.toResponseDTO(any())).thenReturn(tipoUsuarioResponse);

            List<TipoUsuarioResponseDTO> result = tipoUsuarioService.findAll();

            assertNotNull(result);
            assertEquals(2, result.size());
            verify(tipoUsuarioRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando nenhum tipo de usuario existe")
        void shouldReturnEmptyListWhenNoTipoUsuariosExist() {
            when(tipoUsuarioRepository.findAll()).thenReturn(List.of());

            List<TipoUsuarioResponseDTO> result = tipoUsuarioService.findAll();

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(tipoUsuarioRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Testes de Salvamento (Save)")
    class SaveTests {

        @Test
        @DisplayName("Deve salvar tipo de usuario com sucesso")
        void shouldSaveTipoUsuarioSuccessfully() {
            TipoUsuarioRequestDTO request = new TipoUsuarioRequestDTO("Cliente");

            when(tipoUsuarioMapper.toEntity(request)).thenReturn(tipoUsuario);
            when(tipoUsuarioRepository.findByDescricaoIgnoreCase("Cliente")).thenReturn(Optional.empty());
            when(tipoUsuarioRepository.save(tipoUsuario)).thenReturn(tipoUsuario);
            when(tipoUsuarioMapper.toResponseDTO(tipoUsuario)).thenReturn(tipoUsuarioResponse);

            TipoUsuarioResponseDTO result = tipoUsuarioService.save(request);

            assertNotNull(result);
            verify(tipoUsuarioRepository, times(1)).save(tipoUsuario);
            verify(tipoUsuarioRepository, times(1)).findByDescricaoIgnoreCase("Cliente");
        }

        @Test
        @DisplayName("Deve lançar exceção quando tipo de usuario já existe")
        void shouldThrowExceptionWhenTipoUsuarioAlreadyExists() {
            TipoUsuarioRequestDTO request = new TipoUsuarioRequestDTO("Cliente");

            when(tipoUsuarioMapper.toEntity(request)).thenReturn(tipoUsuario);
            when(tipoUsuarioRepository.findByDescricaoIgnoreCase("Cliente"))
                .thenReturn(Optional.of(tipoUsuario));

            assertThrows(BusinessException.class, () -> tipoUsuarioService.save(request));
            verify(tipoUsuarioRepository, times(1)).findByDescricaoIgnoreCase("Cliente");
            verify(tipoUsuarioRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Testes de Atualização (Update)")
    class UpdateTests {

        @Test
        @DisplayName("Deve atualizar tipo de usuario com sucesso")
        void shouldUpdateTipoUsuarioSuccessfully() {
            TipoUsuarioUpdateDTO updateDTO = new TipoUsuarioUpdateDTO("Cliente Premium");

            when(tipoUsuarioRepository.findById(1L)).thenReturn(Optional.of(tipoUsuario));
            when(tipoUsuarioRepository.findByDescricaoIgnoreCase("Cliente Premium")).thenReturn(Optional.empty());
            when(tipoUsuarioRepository.save(any(TipoUsuario.class))).thenReturn(tipoUsuario);
            when(tipoUsuarioMapper.toResponseDTO(tipoUsuario)).thenReturn(tipoUsuarioResponse);

            TipoUsuarioResponseDTO result = tipoUsuarioService.update(1L, updateDTO);

            assertNotNull(result);
            verify(tipoUsuarioRepository, times(1)).findById(1L);
            verify(tipoUsuarioRepository, times(1)).findByDescricaoIgnoreCase("Cliente Premium");
            verify(tipoUsuarioMapper, times(1)).updateEntityFromDTO(updateDTO, tipoUsuario);
            verify(tipoUsuarioRepository, times(1)).save(any(TipoUsuario.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando tipo de usuario não encontrado na atualização")
        void shouldThrowExceptionWhenTipoUsuarioNotFoundOnUpdate() {
            TipoUsuarioUpdateDTO updateDTO = new TipoUsuarioUpdateDTO("Cliente Premium");

            when(tipoUsuarioRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(BusinessException.class, () -> tipoUsuarioService.update(999L, updateDTO));
            verify(tipoUsuarioRepository, times(1)).findById(999L);
        }

        @Test
        @DisplayName("Deve lançar exceção quando nova descrição já existe na atualização")
        void shouldThrowExceptionWhenNewDescriptionAlreadyExists() {
            TipoUsuario tipoUsuarioExistente = new TipoUsuario(2L, "Admin", null);
            TipoUsuarioUpdateDTO updateDTO = new TipoUsuarioUpdateDTO("Admin");

            when(tipoUsuarioRepository.findById(1L)).thenReturn(Optional.of(tipoUsuario));
            when(tipoUsuarioRepository.findByDescricaoIgnoreCase("Admin"))
                .thenReturn(Optional.of(tipoUsuarioExistente));

            assertThrows(BusinessException.class, () -> tipoUsuarioService.update(1L, updateDTO));
            verify(tipoUsuarioRepository, times(1)).findById(1L);
            verify(tipoUsuarioRepository, times(1)).findByDescricaoIgnoreCase("Admin");
            verify(tipoUsuarioRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve permitir atualizar com descrição diferente")
        void shouldAllowUpdateWithDifferentDescription() {
            TipoUsuarioUpdateDTO updateDTO = new TipoUsuarioUpdateDTO("Cliente VIP");

            when(tipoUsuarioRepository.findById(1L)).thenReturn(Optional.of(tipoUsuario));
            when(tipoUsuarioRepository.findByDescricaoIgnoreCase("Cliente VIP"))
                .thenReturn(Optional.empty());
            when(tipoUsuarioRepository.save(any(TipoUsuario.class))).thenReturn(tipoUsuario);
            when(tipoUsuarioMapper.toResponseDTO(tipoUsuario)).thenReturn(tipoUsuarioResponse);

            TipoUsuarioResponseDTO result = tipoUsuarioService.update(1L, updateDTO);

            assertNotNull(result);
            verify(tipoUsuarioRepository, times(1)).save(any(TipoUsuario.class));
        }
    }

    @Nested
    @DisplayName("Testes de Deleção (Delete)")
    class DeleteTests {

        @Test
        @DisplayName("Deve deletar tipo de usuario com sucesso")
        void shouldDeleteTipoUsuarioSuccessfully() {
            when(tipoUsuarioRepository.findById(1L)).thenReturn(Optional.of(tipoUsuario));
            doNothing().when(tipoUsuarioRepository).deleteById(1L);

            tipoUsuarioService.delete(1L);

            verify(tipoUsuarioRepository, times(1)).findById(1L);
            verify(tipoUsuarioRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("Deve lançar exceção quando deletando tipo de usuario que não existe")
        void shouldThrowExceptionWhenDeletingNonExistentTipoUsuario() {
            when(tipoUsuarioRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(BusinessException.class, () -> tipoUsuarioService.delete(999L));
            verify(tipoUsuarioRepository, times(1)).findById(999L);
            verify(tipoUsuarioRepository, never()).deleteById(any());
        }

        @Test
        @DisplayName("Deve verificar se tipo de usuario existe antes de deletar")
        void shouldVerifyExistenceBeforeDelete() {
            when(tipoUsuarioRepository.findById(1L)).thenReturn(Optional.of(tipoUsuario));
            doNothing().when(tipoUsuarioRepository).deleteById(1L);

            tipoUsuarioService.delete(1L);

            verify(tipoUsuarioRepository, times(1)).findById(1L);
            verify(tipoUsuarioRepository, times(1)).deleteById(1L);
        }
    }

}
