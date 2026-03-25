package br.com.fiap.challenge.interfaces.controller;

import br.com.fiap.challenge.application.exception.BusinessException;
import br.com.fiap.challenge.application.exception.ErrorCode;
import br.com.fiap.challenge.application.service.RestauranteService;
import br.com.fiap.challenge.interfaces.dto.request.RestauranteRequest;
import br.com.fiap.challenge.interfaces.dto.request.RestauranteUpdateRequest;
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
@DisplayName("RestauranteController - Integration Tests")
class RestauranteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RestauranteService restauranteService;

    @Nested
    @DisplayName("GET /api/v1/restaurantes/{id}")
    class GetByIdTests {

        @Test
        @DisplayName("Deve retornar restaurante com status 200")
        void shouldReturnRestauranteWithStatus200() throws Exception {
            var response = TestData.createRestauranteResponse();
            when(restauranteService.findById(1L)).thenReturn(response);

            mockMvc.perform(get("/api/v1/restaurantes/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.nome").value("Pizzaria Bella"));

            verify(restauranteService, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Deve retornar 404 quando restaurante não encontrado")
        void shouldReturn404WhenNotFound() throws Exception {
            when(restauranteService.findById(999L))
                    .thenThrow(new BusinessException(ErrorCode.RESTAURANT_NOT_FOUND, HttpStatus.NOT_FOUND));

            mockMvc.perform(get("/api/v1/restaurantes/999"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/restaurantes")
    class GetAllTests {

        @Test
        @DisplayName("Deve retornar lista de restaurantes com status 200")
        void shouldReturnListWithStatus200() throws Exception {
            var response = TestData.createRestauranteResponse();
            when(restauranteService.findAll()).thenReturn(List.of(response));

            mockMvc.perform(get("/api/v1/restaurantes"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].nome").value("Pizzaria Bella"));

            verify(restauranteService, times(1)).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há restaurantes")
        void shouldReturnEmptyList() throws Exception {
            when(restauranteService.findAll()).thenReturn(List.of());

            mockMvc.perform(get("/api/v1/restaurantes"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
        }
    }

    @Nested
    @DisplayName("POST /api/v1/restaurantes")
    class PostTests {

        @Test
        @DisplayName("Deve criar restaurante com status 201")
        void shouldCreateWithStatus201() throws Exception {
            var request = TestData.createRestauranteRequest();
            var response = TestData.createRestauranteResponse();
            when(restauranteService.save(any(RestauranteRequest.class))).thenReturn(response);

            String body = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/restaurantes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.nome").value("Pizzaria Bella"));

            verify(restauranteService, times(1)).save(any(RestauranteRequest.class));
        }

        @Test
        @DisplayName("Deve retornar 400 com nome vazio")
        void shouldReturnStatus400WithEmptyName() throws Exception {
            var request = new RestauranteRequest(
                    "",
                    "Italiana",
                    "08:00-22:00",
                    1L,
                    List.of(new br.com.fiap.challenge.interfaces.dto.request.EnderecoRequestDTO(
                            "Rua das Flores", "500", "Sala 10", "Centro",
                            "12345678", "São Paulo", "SP"
                    ))
            );
            String body = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/restaurantes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve retornar 409 quando restaurante já existe")
        void shouldReturnStatus409WhenRestauranteExists() throws Exception {
            var request = TestData.createRestauranteRequest();
            when(restauranteService.save(any(RestauranteRequest.class)))
                    .thenThrow(new BusinessException(ErrorCode.RESTAURANT_ALREADY_EXISTS, HttpStatus.CONFLICT));

            String body = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/restaurantes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/restaurantes/{id}")
    class PutTests {

        @Test
        @DisplayName("Deve atualizar restaurante com status 200")
        void shouldUpdateWithStatus200() throws Exception {
            var update = TestData.createRestauranteUpdate();
            var response = TestData.createRestauranteResponse();
            when(restauranteService.update(eq(1L), any(RestauranteUpdateRequest.class))).thenReturn(response);

            String body = objectMapper.writeValueAsString(update);

            mockMvc.perform(put("/api/v1/restaurantes/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.nome").value("Pizzaria Bella"));

            verify(restauranteService, times(1)).update(eq(1L), any(RestauranteUpdateRequest.class));
        }

        @Test
        @DisplayName("Deve retornar 404 quando restaurante não existe para atualizar")
        void shouldReturn404WhenRestauranteNotFoundForUpdate() throws Exception {
            var update = TestData.createRestauranteUpdate();
            when(restauranteService.update(eq(999L), any(RestauranteUpdateRequest.class)))
                    .thenThrow(new BusinessException(ErrorCode.RESTAURANT_NOT_FOUND, HttpStatus.NOT_FOUND));

            String body = objectMapper.writeValueAsString(update);

            mockMvc.perform(put("/api/v1/restaurantes/999")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/restaurantes/{id}")
    class DeleteTests {

        @Test
        @DisplayName("Deve deletar restaurante com status 204")
        void shouldDeleteWithStatus204() throws Exception {
            doNothing().when(restauranteService).delete(1L);

            mockMvc.perform(delete("/api/v1/restaurantes/1"))
                    .andExpect(status().isNoContent());

            verify(restauranteService, times(1)).delete(1L);
        }

        @Test
        @DisplayName("Deve retornar 404 quando restaurante não existe para deletar")
        void shouldReturn404WhenRestauranteNotFoundForDelete() throws Exception {
            doThrow(new BusinessException(ErrorCode.RESTAURANT_NOT_FOUND, HttpStatus.NOT_FOUND))
                    .when(restauranteService).delete(999L);

            mockMvc.perform(delete("/api/v1/restaurantes/999"))
                    .andExpect(status().isNotFound());
        }
    }
}





