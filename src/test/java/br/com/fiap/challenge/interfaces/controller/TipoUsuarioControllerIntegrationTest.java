package br.com.fiap.challenge.interfaces.controller;

import br.com.fiap.challenge.application.exception.BusinessException;
import br.com.fiap.challenge.application.exception.ErrorCode;
import br.com.fiap.challenge.application.service.TipoUsuarioService;
import br.com.fiap.challenge.interfaces.dto.request.TipoUsuarioRequestDTO;
import br.com.fiap.challenge.interfaces.dto.response.TipoUsuarioResponseDTO;
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
@DisplayName("TipoUsuarioController - Integration Tests")
class TipoUsuarioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TipoUsuarioService tipoUsuarioService;

    @Nested
    @DisplayName("GET /api/v1/tipo-usuario/{id}")
    class GetByIdTests {

        @Test
        @DisplayName("Deve retornar tipo de usuario com status 200")
        void shouldReturnTipoUsuarioWithStatus200() throws Exception {
            var response = new TipoUsuarioResponseDTO(1L, "USER");
            when(tipoUsuarioService.findById(1L)).thenReturn(response);

            mockMvc.perform(get("/api/v1/tipo-usuario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("USER"));

            verify(tipoUsuarioService, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Deve retornar 404 quando tipo nao encontrado")
        void shouldReturn404WhenNotFound() throws Exception {
            when(tipoUsuarioService.findById(999L))
                .thenThrow(new BusinessException(ErrorCode.USER_TYPE_NOT_FOUND, HttpStatus.NOT_FOUND));

            mockMvc.perform(get("/api/v1/tipo-usuario/999"))
                .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/tipo-usuario")
    class GetAllTests {

        @Test
        @DisplayName("Deve retornar lista com status 200")
        void shouldReturnListWithStatus200() throws Exception {
            var response = new TipoUsuarioResponseDTO(1L, "USER");
            when(tipoUsuarioService.findAll()).thenReturn(List.of(response));

            mockMvc.perform(get("/api/v1/tipo-usuario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

            verify(tipoUsuarioService, times(1)).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia")
        void shouldReturnEmptyList() throws Exception {
            when(tipoUsuarioService.findAll()).thenReturn(List.of());

            mockMvc.perform(get("/api/v1/tipo-usuario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
        }
    }

    @Nested
    @DisplayName("POST /api/v1/tipo-usuario")
    class PostTests {

        @Test
        @DisplayName("Deve criar tipo com status 200")
        void shouldCreateWithStatus200() throws Exception {
            var request = TestData.createTipoUsuarioRequest();
            var response = new TipoUsuarioResponseDTO(1L, "ADMIN");
            when(tipoUsuarioService.save(any())).thenReturn(response);

            String body = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/tipo-usuario")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("ADMIN"));

            verify(tipoUsuarioService, times(1)).save(any());
        }

        @Test
        @DisplayName("Deve retornar 400 com descricao vazia")
        void shouldReturnStatus400WithEmptyDescription() throws Exception {
            var request = new TipoUsuarioRequestDTO("");
            String body = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/tipo-usuario")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/tipo-usuario/{id}")
    class PutTests {

        @Test
        @DisplayName("Deve atualizar com status 201")
        void shouldUpdateWithStatus201() throws Exception {
            var update = TestData.createTipoUsuarioUpdate();
            var response = new TipoUsuarioResponseDTO(1L, "GERENTE");
            when(tipoUsuarioService.update(eq(1L), any())).thenReturn(response);

            String body = objectMapper.writeValueAsString(update);

            mockMvc.perform(put("/api/v1/tipo-usuario/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                .andExpect(status().isCreated());

            verify(tipoUsuarioService, times(1)).update(eq(1L), any());
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/tipo-usuario/{id}")
    class DeleteTests {

        @Test
        @DisplayName("Deve deletar com status 204")
        void shouldDeleteWithStatus204() throws Exception {
            doNothing().when(tipoUsuarioService).delete(1L);

            mockMvc.perform(delete("/api/v1/tipo-usuario/1"))
                .andExpect(status().isNoContent());

            verify(tipoUsuarioService, times(1)).delete(1L);
        }
    }
}

