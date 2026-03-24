package br.com.fiap.challenge.interfaces.controller;

import br.com.fiap.challenge.application.exception.BusinessException;
import br.com.fiap.challenge.application.exception.ErrorCode;
import br.com.fiap.challenge.application.service.ItemCardapioService;
import br.com.fiap.challenge.interfaces.dto.request.ItemCardapioRequest;
import br.com.fiap.challenge.interfaces.dto.request.ItemCardapioUpdateRequest;
import br.com.fiap.challenge.support.TestData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
@DisplayName("ItemCardapioController - Integration Tests")
class ItemCardapioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemCardapioService itemCardapioService;

    @Nested
    @DisplayName("GET /api/v1/item-cardapio/{id}")
    class GetByIdTests {

        @Test
        @DisplayName("Deve retornar item de cardápio com status 200")
        void shouldReturnItemCardapioWithStatus200() throws Exception {
            var response = TestData.createItemCardapioResponse();
            when(itemCardapioService.findById(1L)).thenReturn(response);

            mockMvc.perform(get("/api/v1/item-cardapio/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.nome").value("Moqueca"));

            verify(itemCardapioService, times(1)).findById(1L);
        }

        @Test
        @DisplayName("Deve retornar 404 quando item não encontrado")
        void shouldReturn404WhenNotFound() throws Exception {
            when(itemCardapioService.findById(999L))
                    .thenThrow(new BusinessException(ErrorCode.ITEM_CARDAPIO_NOT_FOUND, HttpStatus.NOT_FOUND));

            mockMvc.perform(get("/api/v1/item-cardapio/999"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/item-cardapio")
    class GetAllTests {

        @Test
        @DisplayName("Deve retornar lista de itens com status 200")
        void shouldReturnListWithStatus200() throws Exception {
            var response = TestData.createItemCardapioResponse();
            when(itemCardapioService.findAll()).thenReturn(List.of(response));

            mockMvc.perform(get("/api/v1/item-cardapio"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].nome").value("Moqueca"));

            verify(itemCardapioService, times(1)).findAll();
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando não há itens")
        void shouldReturnEmptyList() throws Exception {
            when(itemCardapioService.findAll()).thenReturn(List.of());

            mockMvc.perform(get("/api/v1/item-cardapio"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
        }
    }

    @Nested
    @DisplayName("POST /api/v1/item-cardapio")
    class PostTests {

        @Test
        @DisplayName("Deve criar item de cardápio com status 201")
        void shouldCreateWithStatus201() throws Exception {
            var request = TestData.createItemCardapioRequest();
            var response = TestData.createItemCardapioResponse();
            when(itemCardapioService.save(any(ItemCardapioRequest.class))).thenReturn(response);

            String body = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/item-cardapio")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.nome").value("Moqueca"));

            verify(itemCardapioService, times(1)).save(any(ItemCardapioRequest.class));
        }

        @Test
        @DisplayName("Deve retornar 400 com nome vazio")
        void shouldReturnStatus400WithEmptyName() throws Exception {
            var request = new ItemCardapioRequest(
                    "",
                    "Descrição",
                    java.math.BigDecimal.valueOf(45.50),
                    false,
                    "foto.jpg",
                    1L
            );
            String body = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/api/v1/item-cardapio")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("PUT /api/v1/item-cardapio/{id}")
    class PutTests {

        @Test
        @DisplayName("Deve atualizar item de cardápio com status 200")
        void shouldUpdateWithStatus200() throws Exception {
            var update = TestData.createItemCardapioUpdate();
            var response = TestData.createItemCardapioResponse();
            when(itemCardapioService.update(eq(1L), any(ItemCardapioUpdateRequest.class))).thenReturn(response);

            String body = objectMapper.writeValueAsString(update);

            mockMvc.perform(put("/api/v1/item-cardapio/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.nome").value("Moqueca"));

            verify(itemCardapioService, times(1)).update(eq(1L), any(ItemCardapioUpdateRequest.class));
        }

        @Test
        @DisplayName("Deve retornar 404 quando item não existe para atualizar")
        void shouldReturn404WhenItemNotFoundForUpdate() throws Exception {
            var update = TestData.createItemCardapioUpdate();
            when(itemCardapioService.update(eq(999L), any(ItemCardapioUpdateRequest.class)))
                    .thenThrow(new BusinessException(ErrorCode.ITEM_CARDAPIO_NOT_FOUND, HttpStatus.NOT_FOUND));

            String body = objectMapper.writeValueAsString(update);

            mockMvc.perform(put("/api/v1/item-cardapio/999")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("DELETE /api/v1/item-cardapio/{id}")
    class DeleteTests {

        @Test
        @DisplayName("Deve deletar item de cardápio com status 204")
        void shouldDeleteWithStatus204() throws Exception {
            doNothing().when(itemCardapioService).delete(1L);

            mockMvc.perform(delete("/api/v1/item-cardapio/1"))
                    .andExpect(status().isNoContent());

            verify(itemCardapioService, times(1)).delete(1L);
        }

        @Test
        @DisplayName("Deve retornar 404 quando item não existe para deletar")
        void shouldReturn404WhenItemNotFoundForDelete() throws Exception {
            doThrow(new BusinessException(ErrorCode.ITEM_CARDAPIO_NOT_FOUND, HttpStatus.NOT_FOUND))
                    .when(itemCardapioService).delete(999L);

            mockMvc.perform(delete("/api/v1/item-cardapio/999"))
                    .andExpect(status().isNotFound());
        }
    }
}





