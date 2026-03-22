package br.com.fiap.challenge.interfaces.controller;

import br.com.fiap.challenge.application.exception.BusinessException;
import br.com.fiap.challenge.application.exception.ErrorCode;
import br.com.fiap.challenge.application.service.TipoUsuarioService;
import br.com.fiap.challenge.interfaces.dto.request.TipoUsuarioRequestDTO;
import br.com.fiap.challenge.interfaces.dto.request.TipoUsuarioUpdateDTO;
import br.com.fiap.challenge.interfaces.dto.response.TipoUsuarioResponseDTO;
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
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("TipoUsuarioController")
class TipoUsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TipoUsuarioService tipoUsuarioService;

    private TipoUsuarioResponseDTO tipoUsuarioResponse;
    private TipoUsuarioRequestDTO tipoUsuarioRequest;
    private TipoUsuarioUpdateDTO tipoUsuarioUpdate;

    @BeforeEach
    void setUp() {
        tipoUsuarioResponse = new TipoUsuarioResponseDTO(1L, "ADMIN");
        tipoUsuarioRequest = new TipoUsuarioRequestDTO("ADMIN");
        tipoUsuarioUpdate = new TipoUsuarioUpdateDTO("ADMIN_UPDATED");
    }

    @Nested
    @DisplayName("Testes do método GET by ID")
    class GetByIdTests {

        @Test
        @DisplayName("Deve retornar TipoUsuario com status 200")
        void shouldReturnTipoUsuarioWithStatus200() throws Exception {
            when(tipoUsuarioService.findById(1L))
                .thenReturn(tipoUsuarioResponse);

            mockMvc.perform(get("/api/v1/tipo-usuario/1")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("ADMIN"));

            verify(tipoUsuarioService, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Deve retornar 404 quando TipoUsuario não existe")
        void shouldReturnStatus404WhenTipoUsuarioNotFound() throws Exception {
            when(tipoUsuarioService.findById(999L))
                .thenThrow(new BusinessException(ErrorCode.USER_TYPE_NOT_FOUND, HttpStatus.NOT_FOUND));

            mockMvc.perform(get("/api/v1/tipo-usuario/999")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

            verify(tipoUsuarioService, times(1)).findById(999L);
        }
    }

    @Nested
    @DisplayName("Testes do método GET all")
    class GetAllTests {

        @Test
        @DisplayName("Deve retornar lista de TipoUsuarios com status 200")
        void shouldReturnListOfTipoUsuariosWithStatus200() throws Exception {
            TipoUsuarioResponseDTO tipoUsuario2 = new TipoUsuarioResponseDTO(2L, "USER");
            List<TipoUsuarioResponseDTO> tipoUsuarios = List.of(tipoUsuarioResponse, tipoUsuario2);

            when(tipoUsuarioService.findAll())
                .thenReturn(tipoUsuarios);

            mockMvc.perform(get("/api/v1/tipo-usuario")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].descricao").value("ADMIN"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].descricao").value("USER"));

            verify(tipoUsuarioService, times(1)).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia com status 200")
        void shouldReturnEmptyListWithStatus200() throws Exception {
            when(tipoUsuarioService.findAll())
                .thenReturn(List.of());

            mockMvc.perform(get("/api/v1/tipo-usuario")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

            verify(tipoUsuarioService, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Testes do método POST")
    class PostTests {

        @Test
        @DisplayName("Deve criar TipoUsuario com status 200")
        void shouldCreateTipoUsuarioWithStatus200() throws Exception {
            when(tipoUsuarioService.save(any(TipoUsuarioRequestDTO.class)))
                .thenReturn(tipoUsuarioResponse);

            String requestBody = objectMapper.writeValueAsString(tipoUsuarioRequest);

            mockMvc.perform(post("/api/v1/tipo-usuario")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("ADMIN"));

            verify(tipoUsuarioService, times(1)).save(any(TipoUsuarioRequestDTO.class));
        }

        @Test
        @DisplayName("Deve retornar 400 quando corpo da requisição é inválido")
        void shouldReturnStatus400WhenRequestBodyIsInvalid() throws Exception {
            String invalidRequest = "{\"descricao\": \"\"}";

            mockMvc.perform(post("/api/v1/tipo-usuario")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidRequest))
                .andExpect(status().isBadRequest());

            verify(tipoUsuarioService, never()).save(any());
        }

        @Test
        @DisplayName("Deve retornar 409 quando TipoUsuario já existe")
        void shouldReturnStatus409WhenTipoUsuarioAlreadyExists() throws Exception {
            when(tipoUsuarioService.save(any(TipoUsuarioRequestDTO.class)))
                .thenThrow(new BusinessException(ErrorCode.USER_TYPE_ALREADY_EXISTS, HttpStatus.CONFLICT));

            String requestBody = objectMapper.writeValueAsString(tipoUsuarioRequest);

            mockMvc.perform(post("/api/v1/tipo-usuario")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isConflict());

            verify(tipoUsuarioService, times(1)).save(any(TipoUsuarioRequestDTO.class));
        }
    }

    @Nested
    @DisplayName("Testes do método PUT")
    class PutTests {

        @Test
        @DisplayName("Deve atualizar TipoUsuario com status 201")
        void shouldUpdateTipoUsuarioWithStatus201() throws Exception {
            TipoUsuarioResponseDTO updatedResponse = new TipoUsuarioResponseDTO(1L, "ADMIN_UPDATED");

            when(tipoUsuarioService.update(eq(1L), any(TipoUsuarioUpdateDTO.class)))
                .thenReturn(updatedResponse);

            String requestBody = objectMapper.writeValueAsString(tipoUsuarioUpdate);

            mockMvc.perform(put("/api/v1/tipo-usuario/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.descricao").value("ADMIN_UPDATED"));

            verify(tipoUsuarioService, times(1)).update(eq(1L), any(TipoUsuarioUpdateDTO.class));
        }

        @Test
        @DisplayName("Deve retornar 404 quando TipoUsuario não existe para atualizar")
        void shouldReturnStatus404WhenTipoUsuarioNotFoundForUpdate() throws Exception {
            when(tipoUsuarioService.update(eq(999L), any(TipoUsuarioUpdateDTO.class)))
                .thenThrow(new BusinessException(ErrorCode.USER_TYPE_NOT_FOUND, HttpStatus.NOT_FOUND));

            String requestBody = objectMapper.writeValueAsString(tipoUsuarioUpdate);

            mockMvc.perform(put("/api/v1/tipo-usuario/999")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isNotFound());

            verify(tipoUsuarioService, times(1)).update(eq(999L), any(TipoUsuarioUpdateDTO.class));
        }

        @Test
        @DisplayName("Deve retornar 409 quando nova descrição já existe")
        void shouldReturnStatus409WhenNewDescricaoAlreadyExists() throws Exception {
            when(tipoUsuarioService.update(eq(1L), any(TipoUsuarioUpdateDTO.class)))
                .thenThrow(new BusinessException(ErrorCode.USER_TYPE_ALREADY_EXISTS, HttpStatus.CONFLICT));

            String requestBody = objectMapper.writeValueAsString(tipoUsuarioUpdate);

            mockMvc.perform(put("/api/v1/tipo-usuario/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isConflict());

            verify(tipoUsuarioService, times(1)).update(eq(1L), any(TipoUsuarioUpdateDTO.class));
        }
    }

    @Nested
    @DisplayName("Testes do método DELETE")
    class DeleteTests {

        @Test
        @DisplayName("Deve deletar TipoUsuario com status 204")
        void shouldDeleteTipoUsuarioWithStatus204() throws Exception {
            doNothing().when(tipoUsuarioService).delete(1L);

            mockMvc.perform(delete("/api/v1/tipo-usuario/1")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

            verify(tipoUsuarioService, times(1)).delete(1L);
        }

        @Test
        @DisplayName("Deve retornar 404 quando TipoUsuario não existe para deletar")
        void shouldReturnStatus404WhenTipoUsuarioNotFoundForDelete() throws Exception {
            doThrow(new BusinessException(ErrorCode.USER_TYPE_NOT_FOUND, HttpStatus.NOT_FOUND))
                .when(tipoUsuarioService).delete(999L);

            mockMvc.perform(delete("/api/v1/tipo-usuario/999")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

            verify(tipoUsuarioService, times(1)).delete(999L);
        }
    }
}

