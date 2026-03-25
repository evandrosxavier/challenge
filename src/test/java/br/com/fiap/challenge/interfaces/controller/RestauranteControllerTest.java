package br.com.fiap.challenge.interfaces.controller;

import br.com.fiap.challenge.application.exception.BusinessException;
import br.com.fiap.challenge.application.exception.ErrorCode;
import br.com.fiap.challenge.application.service.RestauranteService;
import br.com.fiap.challenge.interfaces.dto.request.RestauranteRequest;
import br.com.fiap.challenge.interfaces.dto.request.RestauranteUpdateRequest;
import br.com.fiap.challenge.interfaces.dto.request.EnderecoRequestDTO;
import br.com.fiap.challenge.interfaces.dto.response.RestauranteResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
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
@DisplayName("RestauranteController")
class RestauranteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RestauranteService restauranteService;

    private RestauranteResponse restauranteResponse;
    private RestauranteRequest restauranteRequest;
    private RestauranteUpdateRequest restauranteUpdateRequest;

    @BeforeEach
    void setUp() {
        EnderecoRequestDTO enderecoRequest = new EnderecoRequestDTO(
            "Rua das Flores",
            "123",
            "Apt 456",
            "Centro",
            "01000-000",
            "São Paulo",
            "SP"
        );

        restauranteResponse = new RestauranteResponse(1L, "Pizzaria Bella", "Italiana", "11:00-23:00", null, null, null);
        restauranteRequest = new RestauranteRequest(
            "Pizzaria Bella",
            "Italiana",
            "11:00-23:00",
            1L,
            List.of(enderecoRequest)
        );
        restauranteUpdateRequest = new RestauranteUpdateRequest(
            "Pizzaria Bella Updated",
            "Italiana",
            "11:00-00:00",
            1L,
            List.of()
        );
    }

    private EnderecoRequestDTO createEnderecoRequest() {
        return new EnderecoRequestDTO(
            "Rua das Flores",
            "123",
            "Apt 456",
            "Centro",
            "01000-000",
            "São Paulo",
            "SP"
        );
    }

    @Nested
    @DisplayName("Testes do método GET by ID")
    class GetByIdTests {

        @Test
        @DisplayName("Deve retornar Restaurante com status 200")
        void shouldReturnRestauranteWithStatus200() throws Exception {
            when(restauranteService.findById(1L))
                .thenReturn(restauranteResponse);

            mockMvc.perform(get("/api/v1/restaurantes/1")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Pizzaria Bella"));

            verify(restauranteService, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Deve retornar 404 quando Restaurante não existe")
        void shouldReturnStatus404WhenRestauranteNotFound() throws Exception {
            when(restauranteService.findById(999L))
                .thenThrow(new BusinessException(ErrorCode.RESTAURANT_NOT_FOUND, HttpStatus.NOT_FOUND));

            mockMvc.perform(get("/api/v1/restaurantes/999")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

            verify(restauranteService, times(1)).findById(999L);
        }
    }

    @Nested
    @DisplayName("Testes do método GET all")
    class GetAllTests {

        @Test
        @DisplayName("Deve retornar lista de Restaurantes com status 200")
        void shouldReturnListOfRestaurantesWithStatus200() throws Exception {
            RestauranteResponse restaurante2 = new RestauranteResponse(2L, "Churrascaria X", "Brasileira", "12:00-00:00", null, null, null);
            List<RestauranteResponse> restaurantes = List.of(restauranteResponse, restaurante2);

            when(restauranteService.findAll())
                .thenReturn(restaurantes);

            mockMvc.perform(get("/api/v1/restaurantes")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("Pizzaria Bella"))
                .andExpect(jsonPath("$[1].id").value(2));

            verify(restauranteService, times(1)).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia com status 200")
        void shouldReturnEmptyListWithStatus200() throws Exception {
            when(restauranteService.findAll())
                .thenReturn(List.of());

            mockMvc.perform(get("/api/v1/restaurantes")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

            verify(restauranteService, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Testes do método POST")
    class PostTests {

        @Test
        @DisplayName("Deve retornar 400 quando corpo da requisição é inválido")
        void shouldReturnStatus400WhenRequestBodyIsInvalid() throws Exception {
            String invalidRequest = "{\"nome\": \"\"}";

            mockMvc.perform(post("/api/v1/restaurantes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidRequest))
                .andExpect(status().isBadRequest());

            verify(restauranteService, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Testes do método PUT")
    class PutTests {

        @Test
        @DisplayName("Deve atualizar Restaurante com status 200")
        void shouldUpdateRestauranteWithStatus201() throws Exception {
            RestauranteResponse updatedResponse = new RestauranteResponse(1L, "Pizzaria Bella Updated", "Italiana", "11:00-00:00", null, null, null);

            when(restauranteService.update(eq(1L), any(RestauranteUpdateRequest.class)))
                .thenReturn(updatedResponse);

            String requestBody = objectMapper.writeValueAsString(restauranteUpdateRequest);

            mockMvc.perform(put("/api/v1/restaurantes/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Pizzaria Bella Updated"));

            verify(restauranteService, times(1)).update(eq(1L), any(RestauranteUpdateRequest.class));
        }

        @Test
        @DisplayName("Deve retornar 404 quando Restaurante não existe para atualizar")
        void shouldReturnStatus404WhenRestauranteNotFoundForUpdate() throws Exception {
            when(restauranteService.update(eq(999L), any(RestauranteUpdateRequest.class)))
                .thenThrow(new BusinessException(ErrorCode.RESTAURANT_NOT_FOUND, HttpStatus.NOT_FOUND));

            String requestBody = objectMapper.writeValueAsString(restauranteUpdateRequest);

            mockMvc.perform(put("/api/v1/restaurantes/999")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isNotFound());

            verify(restauranteService, times(1)).update(eq(999L), any(RestauranteUpdateRequest.class));
        }
    }

    @Nested
    @DisplayName("Testes do método DELETE")
    class DeleteTests {

        @Test
        @DisplayName("Deve deletar Restaurante com status 204")
        void shouldDeleteRestauranteWithStatus204() throws Exception {
            doNothing().when(restauranteService).delete(1L);

            mockMvc.perform(delete("/api/v1/restaurantes/1")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

            verify(restauranteService, times(1)).delete(1L);
        }

        @Test
        @DisplayName("Deve retornar 404 quando Restaurante não existe para deletar")
        void shouldReturnStatus404WhenRestauranteNotFoundForDelete() throws Exception {
            doThrow(new BusinessException(ErrorCode.RESTAURANT_NOT_FOUND, HttpStatus.NOT_FOUND))
                .when(restauranteService).delete(999L);

            mockMvc.perform(delete("/api/v1/restaurantes/999")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

            verify(restauranteService, times(1)).delete(999L);
        }
    }

    @Nested
    @DisplayName("Testes de Validação de Request")
    class RequestValidationTests {

        @Test
        @DisplayName("Deve retornar 400 quando nome está vazio")
        void shouldReturnStatus400WhenNomeIsEmpty() throws Exception {
            String invalidRequest = "{\"nome\": \"\", \"tipoCozinha\": \"Italiana\", \"horarioFuncionamento\": \"11:00-23:00\"}";

            mockMvc.perform(post("/api/v1/restaurantes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidRequest))
                .andExpect(status().isBadRequest());

            verify(restauranteService, never()).save(any());
        }

        @Test
        @DisplayName("Deve retornar 400 quando tipoCozinha está vazio")
        void shouldReturnStatus400WhenTipoCozinhaIsEmpty() throws Exception {
            String invalidRequest = "{\"nome\": \"Pizzaria\", \"tipoCozinha\": \"\", \"horarioFuncionamento\": \"11:00-23:00\"}";

            mockMvc.perform(post("/api/v1/restaurantes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidRequest))
                .andExpect(status().isBadRequest());

            verify(restauranteService, never()).save(any());
        }

        @Test
        @DisplayName("Deve retornar 400 quando horarioFuncionamento está vazio")
        void shouldReturnStatus400WhenHorarioFuncionamentoIsEmpty() throws Exception {
            String invalidRequest = "{\"nome\": \"Pizzaria\", \"tipoCozinha\": \"Italiana\", \"horarioFuncionamento\": \"\"}";

            mockMvc.perform(post("/api/v1/restaurantes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidRequest))
                .andExpect(status().isBadRequest());

            verify(restauranteService, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Testes de Update com Validações")
    class UpdateValidationTests {

        @Test
        @DisplayName("Deve retornar 400 quando corpo do update está vazio")
        void shouldReturnStatus400WhenUpdateBodyIsEmpty() throws Exception {
            String invalidRequest = "{\"nome\": \"\"}";

            mockMvc.perform(put("/api/v1/restaurantes/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidRequest))
                .andExpect(status().isBadRequest());

            verify(restauranteService, never()).update(anyLong(), any());
        }

        @Test
        @DisplayName("Deve retornar 200 ao atualizar com sucesso")
        void shouldReturnStatus200OnSuccessfulUpdate() throws Exception {
            RestauranteResponse updatedResponse = new RestauranteResponse(1L, "Pizzaria Bella Updated", "Italiana", "11:00-00:00", null, null, null);

            when(restauranteService.update(eq(1L), any(RestauranteUpdateRequest.class)))
                .thenReturn(updatedResponse);

            String requestBody = objectMapper.writeValueAsString(restauranteUpdateRequest);

            mockMvc.perform(put("/api/v1/restaurantes/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Pizzaria Bella Updated"));

            verify(restauranteService, times(1)).update(eq(1L), any(RestauranteUpdateRequest.class));
        }
    }

    @Nested
    @DisplayName("Testes de Integração e Fluxos Completos")
    class IntegrationTests {

        @Test
        @DisplayName("Deve validar fluxo completo: buscar, atualizar e deletar")
        void shouldValidateCompleteFlowWithoutCreate() throws Exception {
            when(restauranteService.findById(1L))
                .thenReturn(restauranteResponse);

            mockMvc.perform(get("/api/v1/restaurantes/1")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

            RestauranteResponse updatedResponse = new RestauranteResponse(1L, "Pizzaria Bella Updated", "Italiana", "11:00-00:00", null, null, null);

            when(restauranteService.update(eq(1L), any(RestauranteUpdateRequest.class)))
                .thenReturn(updatedResponse);

            String updateBody = objectMapper.writeValueAsString(restauranteUpdateRequest);
            mockMvc.perform(put("/api/v1/restaurantes/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(updateBody))
                .andExpect(status().isOk());

            doNothing().when(restauranteService).delete(1L);

            mockMvc.perform(delete("/api/v1/restaurantes/1")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

            verify(restauranteService, times(1)).findById(1L);
            verify(restauranteService, times(1)).update(eq(1L), any(RestauranteUpdateRequest.class));
            verify(restauranteService, times(1)).delete(1L);
        }

        @Test
        @DisplayName("Deve validar múltiplos Restaurantes em findAll")
        void shouldValidateMultipleRestaurantesInFindAll() throws Exception {
            RestauranteResponse restaurante2 = new RestauranteResponse(2L, "Churrascaria X", "Brasileira", "12:00-00:00", null, null, null);
            RestauranteResponse restaurante3 = new RestauranteResponse(3L, "Sushi Bar", "Japonesa", "18:00-22:00", null, null, null);
            List<RestauranteResponse> restaurantes = List.of(restauranteResponse, restaurante2, restaurante3);

            when(restauranteService.findAll())
                .thenReturn(restaurantes);

            mockMvc.perform(get("/api/v1/restaurantes")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[2].id").value(3));

            verify(restauranteService, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Testes de Verificação de Métodos")
    class VerificationTests {

        @Test
        @DisplayName("Deve verificar que findById é chamado uma única vez")
        void shouldVerifyFindByIdCalledOnce() throws Exception {
            when(restauranteService.findById(1L))
                .thenReturn(restauranteResponse);

            mockMvc.perform(get("/api/v1/restaurantes/1")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

            verify(restauranteService, times(1)).findById(1L);
            verify(restauranteService, never()).findAll();
            verify(restauranteService, never()).delete(anyLong());
        }

        @Test
        @DisplayName("Deve verificar que save nunca é chamado em GET")
        void shouldVerifySaveNeverCalledInGet() throws Exception {
            when(restauranteService.findById(1L))
                .thenReturn(restauranteResponse);

            mockMvc.perform(get("/api/v1/restaurantes/1")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

            verify(restauranteService, never()).save(any());
        }

        @Test
        @DisplayName("Deve verificar que delete é chamado com ID correto")
        void shouldVerifyDeleteCalledWithCorrectId() throws Exception {
            doNothing().when(restauranteService).delete(1L);

            mockMvc.perform(delete("/api/v1/restaurantes/1")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

            verify(restauranteService, times(1)).delete(1L);
            verify(restauranteService, never()).delete(2L);
        }
    }
}
