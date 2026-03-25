package br.com.fiap.challenge.interfaces.controller;

import br.com.fiap.challenge.application.exception.BusinessException;
import br.com.fiap.challenge.application.exception.ErrorCode;
import br.com.fiap.challenge.application.service.UsuarioService;
import br.com.fiap.challenge.interfaces.dto.request.UsuarioCreateRequestDTO;
import br.com.fiap.challenge.interfaces.dto.request.UsuarioUpdateRequestDTO;
import br.com.fiap.challenge.support.TestData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("UsuarioController - Integration Tests")
class UsuarioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UsuarioService usuarioService;

    @Nested
    @DisplayName("GET /api/v1/usuarios/{id}")
    class GetByIdTests {

        @Test
        @DisplayName("Deve retornar usuário com status 200")
        void shouldReturnUsuarioWithStatus200() throws Exception {
            var response = TestData.createUsuarioResponse();
            when(usuarioService.findById(1L)).thenReturn(response);

            mockMvc.perform(get("/api/v1/usuarios/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.nome").value("João Silva"));

            verify(usuarioService, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Deve retornar 404 quando usuário não encontrado")
        void shouldReturn404WhenNotFound() throws Exception {
            when(usuarioService.findById(999L))
                    .thenThrow(new BusinessException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

            mockMvc.perform(get("/api/v1/usuarios/999"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/usuarios")
    class GetAllTests {

        @Test
        @DisplayName("Deve retornar lista de usuários com status 200")
        void shouldReturnListWithStatus200() throws Exception {
            var response = TestData.createUsuarioResponse();
            when(usuarioService.findAll()).thenReturn(List.of(response));

            mockMvc.perform(get("/api/v1/usuarios"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].nome").value("João Silva"));

            verify(usuarioService, times(1)).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia")
        void shouldReturnEmptyList() throws Exception {
            when(usuarioService.findAll()).thenReturn(List.of());

            mockMvc.perform(get("/api/v1/usuarios"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
        }

        @Test
        @DisplayName("Deve buscar usuários por nome")
        void shouldSearchUsuariosByName() throws Exception {
            var response = TestData.createUsuarioResponse();
            when(usuarioService.findByNome("João")).thenReturn(List.of(response));

            mockMvc.perform(get("/api/v1/usuarios?nome=João"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].nome").value("João Silva"));

            verify(usuarioService, times(1)).findByNome("João");
        }
    }

    @Nested
    @DisplayName("POST /api/v1/usuarios")
    class PostTests {

        @Test
        @DisplayName("Deve criar usuário com status 201")
        void shouldCreateWithStatus201() throws Exception {
            var request = TestData.createUsuarioRequest();
            var response = TestData.createUsuarioResponse();
            when(usuarioService.save(any(UsuarioCreateRequestDTO.class))).thenReturn(response);

            String body = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.nome").value("João Silva"));

            verify(usuarioService, times(1)).save(any(UsuarioCreateRequestDTO.class));
        }

        @Test
        @DisplayName("Deve retornar 400 com nome vazio")
        void shouldReturnStatus400WithEmptyName() throws Exception {
            var request = new UsuarioCreateRequestDTO(
                    "",
                    "joao@example.com",
                    "joao.silva",
                    "senha123",
                    1L,
                    List.of()
            );
            String body = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve retornar 409 quando email já existe")
        void shouldReturnStatus409WhenEmailExists() throws Exception {
            var request = TestData.createUsuarioRequest();
            when(usuarioService.save(any(UsuarioCreateRequestDTO.class)))
                    .thenThrow(new BusinessException(ErrorCode.EMAIL_ALREADY_EXISTS, HttpStatus.CONFLICT));

            String body = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/usuarios")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/usuarios/{id}")
    class PutTests {

        @Test
        @DisplayName("Deve atualizar usuário com status 200")
        void shouldUpdateWithStatus200() throws Exception {
            var update = TestData.createUsuarioUpdate();
            var response = TestData.createUsuarioResponse();
            when(usuarioService.update(any(UsuarioUpdateRequestDTO.class), eq(1L))).thenReturn(response);

            String body = objectMapper.writeValueAsString(update);

            mockMvc.perform(put("/api/v1/usuarios/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.nome").value("João Silva"));

            verify(usuarioService, times(1)).update(any(UsuarioUpdateRequestDTO.class), eq(1L));
        }

        @Test
        @DisplayName("Deve retornar 404 quando usuário não existe para atualizar")
        void shouldReturn404WhenUsuarioNotFoundForUpdate() throws Exception {
            var update = TestData.createUsuarioUpdate();
            when(usuarioService.update(any(UsuarioUpdateRequestDTO.class), eq(999L)))
                    .thenThrow(new BusinessException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND));

            String body = objectMapper.writeValueAsString(update);

            mockMvc.perform(put("/api/v1/usuarios/999")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/usuarios/{id}")
    class DeleteTests {

        @Test
        @DisplayName("Deve deletar usuário com status 204")
        void shouldDeleteWithStatus204() throws Exception {
            doNothing().when(usuarioService).delete(1L);

            mockMvc.perform(delete("/api/v1/usuarios/1"))
                    .andExpect(status().isNoContent());

            verify(usuarioService, times(1)).delete(1L);
        }

        @Test
        @DisplayName("Deve retornar 404 quando usuário não existe para deletar")
        void shouldReturn404WhenUsuarioNotFoundForDelete() throws Exception {
            doThrow(new BusinessException(ErrorCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND))
                    .when(usuarioService).delete(999L);

            mockMvc.perform(delete("/api/v1/usuarios/999"))
                    .andExpect(status().isNotFound());
        }
    }
}





