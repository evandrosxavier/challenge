package br.com.fiap.challenge.interfaces.controller;

import br.com.fiap.challenge.application.exception.BusinessException;
import br.com.fiap.challenge.application.exception.ErrorCode;
import br.com.fiap.challenge.application.service.ItemCardapioService;
import br.com.fiap.challenge.interfaces.dto.request.ItemCardapioRequest;
import br.com.fiap.challenge.interfaces.dto.request.ItemCardapioUpdateRequest;
import br.com.fiap.challenge.interfaces.dto.response.ItemCardapioResponse;
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

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ItemCardapioController")
class ItemCardapioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemCardapioService itemCardapioService;

    private ItemCardapioResponse itemCardapioResponse;
    private ItemCardapioRequest itemCardapioRequest;
    private ItemCardapioUpdateRequest itemCardapioUpdateRequest;

    @BeforeEach
    void setUp() {
        itemCardapioResponse = new ItemCardapioResponse(1L, "Moqueca", "Moqueca de Peixe", new BigDecimal("45.50"), true, "https://api.restaurante.com/fotos/moqueca.jpg");
        itemCardapioRequest = new ItemCardapioRequest(
            "Moqueca",
            "Moqueca de Peixe",
            new BigDecimal("45.50"),
            true,
            "https://api.restaurante.com/fotos/moqueca.jpg",
            1L
        );
        itemCardapioUpdateRequest = new ItemCardapioUpdateRequest(
            "Moqueca Updated",
            "Moqueca de Peixe Tradicional",
            new BigDecimal("50.00"),
            false,
            "https://api.restaurante.com/fotos/moqueca_updated.jpg"
        );
    }

    @Nested
    @DisplayName("Testes do método GET by ID")
    class GetByIdTests {

        @Test
        @DisplayName("Deve retornar ItemCardapio com status 200")
        void shouldReturnItemCardapioWithStatus200() throws Exception {
            when(itemCardapioService.findById(1L))
                .thenReturn(itemCardapioResponse);

            mockMvc.perform(get("/api/v1/item-cardapio/1")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Moqueca"));

            verify(itemCardapioService, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Deve retornar 404 quando ItemCardapio não existe")
        void shouldReturnStatus404WhenItemCardapioNotFound() throws Exception {
            when(itemCardapioService.findById(999L))
                .thenThrow(new BusinessException(ErrorCode.ITEM_CARDAPIO_NOT_FOUND, HttpStatus.NOT_FOUND));

            mockMvc.perform(get("/api/v1/item-cardapio/999")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

            verify(itemCardapioService, times(1)).findById(999L);
        }
    }

    @Nested
    @DisplayName("Testes do método GET all")
    class GetAllTests {

        @Test
        @DisplayName("Deve retornar lista de ItemCardapios com status 200")
        void shouldReturnListOfItemCardapiosWithStatus200() throws Exception {
            ItemCardapioResponse item2 = new ItemCardapioResponse(2L, "Pastel", "Pastel de Carne", new BigDecimal("20.00"), false, "https://api.restaurante.com/fotos/pastel.jpg");
            List<ItemCardapioResponse> items = List.of(itemCardapioResponse, item2);

            when(itemCardapioService.findAll())
                .thenReturn(items);

            mockMvc.perform(get("/api/v1/item-cardapio")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("Moqueca"))
                .andExpect(jsonPath("$[1].id").value(2));

            verify(itemCardapioService, times(1)).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia com status 200")
        void shouldReturnEmptyListWithStatus200() throws Exception {
            when(itemCardapioService.findAll())
                .thenReturn(List.of());

            mockMvc.perform(get("/api/v1/item-cardapio")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

            verify(itemCardapioService, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Testes do método POST")
    class PostTests {

        @Test
        @DisplayName("Deve cadastrar ItemCardapio com sucesso e retornar 201")
        void shouldCadastrarItemCardapioWithStatus201() throws Exception {
            when(itemCardapioService.save(any(ItemCardapioRequest.class)))
                .thenReturn(itemCardapioResponse);

            String requestBody = objectMapper.writeValueAsString(itemCardapioRequest);

            mockMvc.perform(post("/api/v1/item-cardapio")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Moqueca"))
                .andExpect(header().exists("Location"));

            verify(itemCardapioService, times(1)).save(any(ItemCardapioRequest.class));
        }

        @Test
        @DisplayName("Deve retornar 400 quando corpo da requisição é inválido")
        void shouldReturnStatus400WhenRequestBodyIsInvalid() throws Exception {
            String invalidRequest = "{\"nome\": \"\"}";

            mockMvc.perform(post("/api/v1/item-cardapio")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidRequest))
                .andExpect(status().isBadRequest());

            verify(itemCardapioService, never()).save(any());
        }

        @Test
        @DisplayName("Deve retornar 404 quando Restaurante não existe")
        void shouldReturnStatus404WhenRestauranteNotFound() throws Exception {
            when(itemCardapioService.save(any(ItemCardapioRequest.class)))
                .thenThrow(new BusinessException(ErrorCode.RESTAURANT_NOT_FOUND, HttpStatus.NOT_FOUND));

            String requestBody = objectMapper.writeValueAsString(itemCardapioRequest);

            mockMvc.perform(post("/api/v1/item-cardapio")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isNotFound());

            verify(itemCardapioService, times(1)).save(any(ItemCardapioRequest.class));
        }
    }

    @Nested
    @DisplayName("Testes do método PUT")
    class PutTests {

        @Test
        @DisplayName("Deve atualizar ItemCardapio com status 200")
        void shouldUpdateItemCardapioWithStatus201() throws Exception {
            ItemCardapioResponse updatedResponse = new ItemCardapioResponse(1L, "Moqueca Updated", "Moqueca de Peixe Tradicional", new BigDecimal("50.00"), false, "https://api.restaurante.com/fotos/moqueca_updated.jpg");

            when(itemCardapioService.update(eq(1L), any(ItemCardapioUpdateRequest.class)))
                .thenReturn(updatedResponse);

            String requestBody = objectMapper.writeValueAsString(itemCardapioUpdateRequest);

            mockMvc.perform(put("/api/v1/item-cardapio/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Moqueca Updated"));

            verify(itemCardapioService, times(1)).update(eq(1L), any(ItemCardapioUpdateRequest.class));
        }

        @Test
        @DisplayName("Deve retornar 404 quando ItemCardapio não existe para atualizar")
        void shouldReturnStatus404WhenItemCardapioNotFoundForUpdate() throws Exception {
            when(itemCardapioService.update(eq(999L), any(ItemCardapioUpdateRequest.class)))
                .thenThrow(new BusinessException(ErrorCode.ITEM_CARDAPIO_NOT_FOUND, HttpStatus.NOT_FOUND));

            String requestBody = objectMapper.writeValueAsString(itemCardapioUpdateRequest);

            mockMvc.perform(put("/api/v1/item-cardapio/999")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isNotFound());

            verify(itemCardapioService, times(1)).update(eq(999L), any(ItemCardapioUpdateRequest.class));
        }
    }

    @Nested
    @DisplayName("Testes do método DELETE")
    class DeleteTests {

        @Test
        @DisplayName("Deve deletar ItemCardapio com status 204")
        void shouldDeleteItemCardapioWithStatus204() throws Exception {
            doNothing().when(itemCardapioService).delete(1L);

            mockMvc.perform(delete("/api/v1/item-cardapio/1")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

            verify(itemCardapioService, times(1)).delete(1L);
        }

        @Test
        @DisplayName("Deve retornar 404 quando ItemCardapio não existe para deletar")
        void shouldReturnStatus404WhenItemCardapioNotFoundForDelete() throws Exception {
            doThrow(new BusinessException(ErrorCode.ITEM_CARDAPIO_NOT_FOUND, HttpStatus.NOT_FOUND))
                .when(itemCardapioService).delete(999L);

            mockMvc.perform(delete("/api/v1/item-cardapio/999")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

            verify(itemCardapioService, times(1)).delete(999L);
        }
    }
}

