package br.com.fiap.challenge.interfaces.controller;

import br.com.fiap.challenge.application.exception.BusinessException;
import br.com.fiap.challenge.application.exception.ErrorCode;
import br.com.fiap.challenge.application.service.UsuarioService;
import br.com.fiap.challenge.interfaces.dto.request.EnderecoRequestDTO;
import br.com.fiap.challenge.interfaces.dto.request.UsuarioCreateRequestDTO;
import br.com.fiap.challenge.interfaces.dto.request.UsuarioUpdateRequestDTO;
import br.com.fiap.challenge.interfaces.dto.request.UsuarioUpdateSenhaDTO;
import br.com.fiap.challenge.interfaces.dto.response.UsuarioResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("UsuarioController")
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuarioService usuarioService;

    private UsuarioResponseDTO usuarioResponse;
    private UsuarioCreateRequestDTO usuarioCreateRequest;
    private UsuarioUpdateRequestDTO usuarioUpdateRequest;
    private UsuarioUpdateSenhaDTO usuarioUpdateSenhaDTO;

    @BeforeEach
    void setUp() {
        usuarioResponse = new UsuarioResponseDTO(1L, "João Silva", "joao@example.com", null, null);

        EnderecoRequestDTO endereco = new EnderecoRequestDTO(
            "Avenida Paulista",
            "1000",
            "Bela Vista",
            "Apto 101",
            "01310100",
            "São Paulo",
            "SP"
        );

        usuarioCreateRequest = new UsuarioCreateRequestDTO(
            "João Silva",
            "joao@example.com",
            "joao.silva",
            "senha123",
            1L,
            List.of(endereco)
        );
        usuarioUpdateRequest = new UsuarioUpdateRequestDTO(
            "João Silva Updated",
            "joao@example.com",
            "joao.silva",
            1L,
            null
        );
        usuarioUpdateSenhaDTO = new UsuarioUpdateSenhaDTO(
            "senha123",
            "novaSenha123"
        );
    }

    @Nested
    @DisplayName("Testes do método GET by ID")
    class GetByIdTests {

        @Test
        @DisplayName("Deve retornar Usuario com status 200")
        void shouldReturnUsuarioWithStatus200() throws Exception {
            when(usuarioService.findById(1L))
                .thenReturn(usuarioResponse);

            mockMvc.perform(get("/api/v1/usuarios/1")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João Silva"));

            verify(usuarioService, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Deve retornar 404 quando Usuario não existe")
        void shouldReturnStatus404WhenUsuarioNotFound() throws Exception {
            when(usuarioService.findById(999L))
                .thenThrow(new BusinessException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

            mockMvc.perform(get("/api/v1/usuarios/999")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

            verify(usuarioService, times(1)).findById(999L);
        }
    }

    @Nested
    @DisplayName("Testes do método GET all")
    class GetAllTests {

        @Test
        @DisplayName("Deve retornar lista de Usuarios com status 200")
        void shouldReturnListOfUsuariosWithStatus200() throws Exception {
            UsuarioResponseDTO usuario2 = new UsuarioResponseDTO(2L, "Maria Silva", "maria@example.com", null, null);
            List<UsuarioResponseDTO> usuarios = List.of(usuarioResponse, usuario2);

            when(usuarioService.findAll())
                .thenReturn(usuarios);

            mockMvc.perform(get("/api/v1/usuarios")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("João Silva"))
                .andExpect(jsonPath("$[1].id").value(2));

            verify(usuarioService, times(1)).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia com status 200")
        void shouldReturnEmptyListWithStatus200() throws Exception {
            when(usuarioService.findAll())
                .thenReturn(List.of());

            mockMvc.perform(get("/api/v1/usuarios")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

            verify(usuarioService, times(1)).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista filtrada por nome com status 200")
        void shouldReturnFilteredListByNomeWithStatus200() throws Exception {
            List<UsuarioResponseDTO> usuariosFiltrados = List.of(usuarioResponse);

            when(usuarioService.findByNome("João"))
                .thenReturn(usuariosFiltrados);

            mockMvc.perform(get("/api/v1/usuarios")
                    .param("nome", "João")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("João Silva"));

            verify(usuarioService, times(1)).findByNome("João");
            verify(usuarioService, never()).findAll();
        }

        @Test
        @DisplayName("Deve ignorar parâmetro nome vazio e retornar todos")
        void shouldIgnoreEmptyNomeParameterAndReturnAll() throws Exception {
            List<UsuarioResponseDTO> usuarios = List.of(usuarioResponse);

            when(usuarioService.findAll())
                .thenReturn(usuarios);

            mockMvc.perform(get("/api/v1/usuarios")
                    .param("nome", "   ")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

            verify(usuarioService, times(1)).findAll();
            verify(usuarioService, never()).findByNome(anyString());
        }
    }

    @Nested
    @DisplayName("Testes do método POST")
    class PostTests {

        @Test
        @DisplayName("Deve criar novo Usuario com status 201")
        void shouldCreateNewUsuarioWithStatus201() throws Exception {
            UsuarioResponseDTO createdResponse = new UsuarioResponseDTO(1L, "João Silva", "joao@example.com", null, null);

            when(usuarioService.save(any(UsuarioCreateRequestDTO.class)))
                .thenReturn(createdResponse);

            String requestBody = objectMapper.writeValueAsString(usuarioCreateRequest);

            mockMvc.perform(post("/api/v1/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João Silva"));

            verify(usuarioService, times(1)).save(any(UsuarioCreateRequestDTO.class));
        }

        @Test
        @DisplayName("Deve retornar 400 quando corpo da requisição é inválido")
        void shouldReturnStatus400WhenRequestBodyIsInvalid() throws Exception {
            String invalidRequest = "{\"nome\": \"\"}";

            mockMvc.perform(post("/api/v1/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidRequest))
                .andExpect(status().isBadRequest());

            verify(usuarioService, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Testes do método PUT")
    class PutTests {

        @Test
        @DisplayName("Deve atualizar Usuario com status 200")
        void shouldUpdateUsuarioWithStatus200() throws Exception {
            UsuarioResponseDTO updatedResponse = new UsuarioResponseDTO(1L, "João Silva Updated", "joao@example.com", null, null);

            when(usuarioService.update(any(UsuarioUpdateRequestDTO.class), eq(1L)))
                .thenReturn(updatedResponse);

            String requestBody = objectMapper.writeValueAsString(usuarioUpdateRequest);

            mockMvc.perform(put("/api/v1/usuarios/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João Silva Updated"));

            verify(usuarioService, times(1)).update(any(UsuarioUpdateRequestDTO.class), eq(1L));
        }

        @Test
        @DisplayName("Deve retornar 404 quando Usuario não existe para atualizar")
        void shouldReturnStatus404WhenUsuarioNotFoundForUpdate() throws Exception {
            when(usuarioService.update(any(UsuarioUpdateRequestDTO.class), eq(999L)))
                .thenThrow(new BusinessException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

            String requestBody = objectMapper.writeValueAsString(usuarioUpdateRequest);

            mockMvc.perform(put("/api/v1/usuarios/999")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isNotFound());

            verify(usuarioService, times(1)).update(any(UsuarioUpdateRequestDTO.class), eq(999L));
        }

        @Test
        @DisplayName("Deve retornar 409 quando novo email já existe")
        void shouldReturnStatus409WhenNewEmailAlreadyExists() throws Exception {
            when(usuarioService.update(any(UsuarioUpdateRequestDTO.class), eq(1L)))
                .thenThrow(new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS, HttpStatus.CONFLICT));

            String requestBody = objectMapper.writeValueAsString(usuarioUpdateRequest);

            mockMvc.perform(put("/api/v1/usuarios/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isConflict());
        }
    }

    @Nested
    @DisplayName("Testes do método DELETE")
    class DeleteTests {

        @Test
        @DisplayName("Deve deletar Usuario com status 204")
        void shouldDeleteUsuarioWithStatus204() throws Exception {
            doNothing().when(usuarioService).delete(1L);

            mockMvc.perform(delete("/api/v1/usuarios/1")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

            verify(usuarioService, times(1)).delete(1L);
        }

        @Test
        @DisplayName("Deve retornar 404 quando Usuario não existe para deletar")
        void shouldReturnStatus404WhenUsuarioNotFoundForDelete() throws Exception {
            doThrow(new BusinessException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND))
                .when(usuarioService).delete(999L);

            mockMvc.perform(delete("/api/v1/usuarios/999")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

            verify(usuarioService, times(1)).delete(999L);
        }
    }

    @Nested
    @DisplayName("Testes do método PATCH - Atualizar Senha")
    class PatchSenhaTests {

        @Test
        @DisplayName("Deve atualizar senha com status 204")
        void shouldUpdateSenhaWithStatus204() throws Exception {
            doNothing().when(usuarioService).updateSenha(any(UsuarioUpdateSenhaDTO.class), eq(1L));

            String requestBody = objectMapper.writeValueAsString(usuarioUpdateSenhaDTO);

            mockMvc.perform(patch("/api/v1/usuarios/1/senha")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isNoContent());

            verify(usuarioService, times(1)).updateSenha(any(UsuarioUpdateSenhaDTO.class), eq(1L));
        }

        @Test
        @DisplayName("Deve retornar 400 quando senha atual está incorreta")
        void shouldReturnStatus400WhenCurrentPasswordIsInvalid() throws Exception {
            doThrow(new BusinessException(ErrorCode.INVALID_PASSWORD, HttpStatus.BAD_REQUEST))
                .when(usuarioService).updateSenha(any(UsuarioUpdateSenhaDTO.class), eq(1L));

            String requestBody = objectMapper.writeValueAsString(usuarioUpdateSenhaDTO);

            mockMvc.perform(patch("/api/v1/usuarios/1/senha")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isBadRequest());

            verify(usuarioService, times(1)).updateSenha(any(UsuarioUpdateSenhaDTO.class), eq(1L));
        }

        @Test
        @DisplayName("Deve retornar 404 quando Usuario não existe para atualizar senha")
        void shouldReturnStatus404WhenUsuarioNotFoundForUpdateSenha() throws Exception {
            doThrow(new BusinessException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND))
                .when(usuarioService).updateSenha(any(UsuarioUpdateSenhaDTO.class), eq(999L));

            String requestBody = objectMapper.writeValueAsString(usuarioUpdateSenhaDTO);

            mockMvc.perform(patch("/api/v1/usuarios/999/senha")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isNotFound());

            verify(usuarioService, times(1)).updateSenha(any(UsuarioUpdateSenhaDTO.class), eq(999L));
        }
    }
}

