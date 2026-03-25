package br.com.fiap.challenge.application.service;

import br.com.fiap.challenge.application.exception.BusinessException;
import br.com.fiap.challenge.application.exception.ErrorCode;
import br.com.fiap.challenge.application.port.RestauranteRepositoryPort;
import br.com.fiap.challenge.application.port.UsuarioRepositoryPort;
import br.com.fiap.challenge.domain.entities.EnderecoRestaurante;
import br.com.fiap.challenge.domain.entities.Restaurante;
import br.com.fiap.challenge.domain.entities.Usuario;
import br.com.fiap.challenge.interfaces.dto.request.EnderecoRequestDTO;
import br.com.fiap.challenge.interfaces.dto.request.RestauranteRequest;
import br.com.fiap.challenge.interfaces.dto.request.RestauranteUpdateRequest;
import br.com.fiap.challenge.interfaces.dto.response.RestauranteResponse;
import br.com.fiap.challenge.interfaces.mapper.EnderecoMapper;
import br.com.fiap.challenge.interfaces.mapper.RestauranteMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RestauranteService")
class RestauranteServiceTest {

    @Mock
    private RestauranteRepositoryPort restauranteRepository;

    @Mock
    private RestauranteMapper restauranteMapper;

    @Mock
    private UsuarioRepositoryPort usuarioRepository;

    @Mock
    private EnderecoMapper enderecoMapper;

    @InjectMocks
    private RestauranteService restauranteService;

    private Restaurante restaurante;
    private RestauranteResponse restauranteResponse;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Dono Pizzaria");

        restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNome("Pizzaria Bella");
        restaurante.setEnderecos(new ArrayList<>());

        restauranteResponse = mock(RestauranteResponse.class);
    }

    @Nested
    @DisplayName("Testes de Busca (Find)")
    class FindTests {

        @Test
        @DisplayName("Deve retornar restaurante quando ID existe")
        void shouldReturnRestauranteWhenIdExists() {
            when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
            when(restauranteMapper.toResponseDTO(restaurante)).thenReturn(restauranteResponse);

            RestauranteResponse result = restauranteService.findById(1L);

            assertNotNull(result);
            verify(restauranteRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Deve lançar exceção quando restaurante não encontrado")
        void shouldThrowExceptionWhenRestauranteNotFound() {
            when(restauranteRepository.findById(999L))
                .thenReturn(Optional.empty());

            assertThrows(BusinessException.class, () -> restauranteService.findById(999L));
            verify(restauranteRepository, times(1)).findById(999L);
        }

        @Test
        @DisplayName("Deve retornar lista de restaurantes")
        void shouldReturnListOfRestaurantes() {
            Restaurante restaurante2 = new Restaurante();
            restaurante2.setId(2L);
            restaurante2.setNome("Churrascaria X");

            List<Restaurante> restaurantes = List.of(restaurante, restaurante2);
            RestauranteResponse response2 = mock(RestauranteResponse.class);

            when(restauranteRepository.findAll()).thenReturn(restaurantes);
            when(restauranteMapper.toResponseDTO(any())).thenReturn(restauranteResponse, response2);

            List<RestauranteResponse> result = restauranteService.findAll();

            assertNotNull(result);
            assertEquals(2, result.size());
            verify(restauranteRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando nenhum restaurante existe")
        void shouldReturnEmptyListWhenNoRestaurantesExist() {
            when(restauranteRepository.findAll()).thenReturn(List.of());

            List<RestauranteResponse> result = restauranteService.findAll();

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(restauranteRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Testes de Salvamento (Save)")
    class SaveTests {

        @Test
        @DisplayName("Deve salvar restaurante com sucesso")
        void shouldSaveRestauranteSuccessfully() {
            RestauranteRequest request = new RestauranteRequest(
                "Pizzaria Bella",
                "Italiana",
                "11:00-22:00",
                1L,
                List.of(mock(EnderecoRequestDTO.class))
            );

            when(restauranteMapper.toEntity(request)).thenReturn(restaurante);
            when(restauranteRepository.findByNomeIgnoreCase("Pizzaria Bella")).thenReturn(Optional.empty());
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
            when(restauranteRepository.save(restaurante)).thenReturn(restaurante);
            when(restauranteMapper.toResponseDTO(restaurante)).thenReturn(restauranteResponse);

            RestauranteResponse result = restauranteService.save(request);

            assertNotNull(result);
            verify(restauranteRepository, times(1)).save(restaurante);
            verify(restauranteRepository, times(1)).findByNomeIgnoreCase("Pizzaria Bella");
        }

        @Test
        @DisplayName("Deve lançar exceção quando restaurante já existe")
        void shouldThrowExceptionWhenRestauranteAlreadyExists() {
            RestauranteRequest request = new RestauranteRequest(
                "Pizzaria Bella",
                "Italiana",
                "11:00-22:00",
                1L,
                List.of(mock(EnderecoRequestDTO.class))
            );

            when(restauranteRepository.findByNomeIgnoreCase("Pizzaria Bella"))
                .thenReturn(Optional.of(restaurante));

            assertThrows(BusinessException.class, () -> restauranteService.save(request));
            verify(restauranteRepository, times(1)).findByNomeIgnoreCase("Pizzaria Bella");
            verify(restauranteRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Testes de Atualização (Update)")
    class UpdateTests {

        @Test
        @DisplayName("Deve atualizar restaurante com sucesso")
        void shouldUpdateRestauranteSuccessfully() {
            EnderecoRequestDTO enderecoDTO = new EnderecoRequestDTO(
                "Rua Nova", "100", "Apto 1", "Centro", "01000-000", "São Paulo", "SP"
            );
            RestauranteUpdateRequest updateDTO = new RestauranteUpdateRequest(
                "Pizzaria Bella Premium",
                "Italiana Premium",
                "10:00-23:00",
                1L,
                List.of(enderecoDTO)
            );

            EnderecoRestaurante enderecoRestaurante = new EnderecoRestaurante();
            enderecoRestaurante.setLogradouro("Rua Nova");

            when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
            when(restauranteRepository.findByNomeIgnoreCase("Pizzaria Bella Premium")).thenReturn(Optional.empty());
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
            when(enderecoMapper.toEnderecoRestaurante(any(EnderecoRequestDTO.class))).thenReturn(enderecoRestaurante);
            when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restaurante);
            when(restauranteMapper.toResponseDTO(restaurante)).thenReturn(restauranteResponse);

            RestauranteResponse result = restauranteService.update(1L, updateDTO);

            assertNotNull(result);
            verify(restauranteRepository, times(1)).findById(1L);
            verify(restauranteRepository, times(1)).findByNomeIgnoreCase("Pizzaria Bella Premium");
            verify(restauranteMapper, times(1)).updateEntityFromDTO(updateDTO, restaurante);
            verify(restauranteRepository, times(1)).save(any(Restaurante.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando restaurante não encontrado na atualização")
        void shouldThrowExceptionWhenRestauranteNotFoundOnUpdate() {
            EnderecoRequestDTO enderecoDTO = new EnderecoRequestDTO(
                "Rua Nova", "100", "Apto 1", "Centro", "01000-000", "São Paulo", "SP"
            );
            RestauranteUpdateRequest updateDTO = new RestauranteUpdateRequest(
                "Pizzaria Bella Premium",
                "Italiana Premium",
                "10:00-23:00",
                1L,
                List.of(enderecoDTO)
            );

            when(restauranteRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(BusinessException.class, () -> restauranteService.update(999L, updateDTO));
            verify(restauranteRepository, times(1)).findById(999L);
        }

        @Test
        @DisplayName("Deve lançar exceção quando novo nome já existe na atualização")
        void shouldThrowExceptionWhenNewNameAlreadyExists() {
            Restaurante restauranteExistente = new Restaurante();
            restauranteExistente.setId(2L);
            restauranteExistente.setNome("Churrascaria X");

            EnderecoRequestDTO enderecoDTO = new EnderecoRequestDTO(
                "Rua Nova", "100", "Apto 1", "Centro", "01000-000", "São Paulo", "SP"
            );
            RestauranteUpdateRequest updateDTO = new RestauranteUpdateRequest(
                "Churrascaria X",
                "Brasileira",
                "12:00-23:00",
                1L,
                List.of(enderecoDTO)
            );

            when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
            when(restauranteRepository.findByNomeIgnoreCase("Churrascaria X"))
                .thenReturn(Optional.of(restauranteExistente));

            assertThrows(BusinessException.class, () -> restauranteService.update(1L, updateDTO));
            verify(restauranteRepository, times(1)).findById(1L);
            verify(restauranteRepository, times(1)).findByNomeIgnoreCase("Churrascaria X");
            verify(restauranteRepository, never()).save(any());
        }

        @Test
        @DisplayName("Deve permitir atualizar com nome diferente")
        void shouldAllowUpdateWithDifferentName() {
            EnderecoRequestDTO enderecoDTO = new EnderecoRequestDTO(
                "Rua Nova", "100", "Apto 1", "Centro", "01000-000", "São Paulo", "SP"
            );
            RestauranteUpdateRequest updateDTO = new RestauranteUpdateRequest(
                "Pizzaria Nova",
                "Italiana",
                "11:00-22:00",
                1L,
                List.of(enderecoDTO)
            );

            EnderecoRestaurante enderecoRestaurante = new EnderecoRestaurante();
            enderecoRestaurante.setLogradouro("Rua Nova");

            when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
            when(restauranteRepository.findByNomeIgnoreCase("Pizzaria Nova"))
                .thenReturn(Optional.empty());
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
            when(enderecoMapper.toEnderecoRestaurante(any(EnderecoRequestDTO.class))).thenReturn(enderecoRestaurante);
            when(restauranteRepository.save(any(Restaurante.class))).thenReturn(restaurante);
            when(restauranteMapper.toResponseDTO(restaurante)).thenReturn(restauranteResponse);

            RestauranteResponse result = restauranteService.update(1L, updateDTO);

            assertNotNull(result);
            verify(restauranteRepository, times(1)).save(any(Restaurante.class));
        }
    }

    @Nested
    @DisplayName("Testes de Deleção (Delete)")
    class DeleteTests {

        @Test
        @DisplayName("Deve deletar restaurante com sucesso")
        void shouldDeleteRestauranteSuccessfully() {
            when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
            doNothing().when(restauranteRepository).deleteById(1L);

            restauranteService.delete(1L);

            verify(restauranteRepository, times(1)).findById(1L);
            verify(restauranteRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("Deve lançar exceção quando deletando restaurante que não existe")
        void shouldThrowExceptionWhenDeletingNonExistentRestaurante() {
            when(restauranteRepository.findById(999L)).thenReturn(Optional.empty());

            assertThrows(BusinessException.class, () -> restauranteService.delete(999L));
            verify(restauranteRepository, times(1)).findById(999L);
            verify(restauranteRepository, never()).deleteById(any());
        }

        @Test
        @DisplayName("Deve verificar se restaurante existe antes de deletar")
        void shouldVerifyExistenceBeforeDelete() {
            when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));
            doNothing().when(restauranteRepository).deleteById(1L);

            restauranteService.delete(1L);

            verify(restauranteRepository, times(1)).findById(1L);
            verify(restauranteRepository, times(1)).deleteById(1L);
        }
    }

}



