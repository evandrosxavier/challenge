package br.com.fiap.challenge.controller;


import br.com.fiap.challenge.dto.request.ItemCardapioRequest;
import br.com.fiap.challenge.dto.request.ItemCardapioUpdateRequest;
import br.com.fiap.challenge.dto.response.ItemCardapioResponse;
import br.com.fiap.challenge.service.ItemCardapioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/item-cardapio")
@Tag(name = "Item de Cardápio", description = "API responsável pela gestão dos itens do cardápio dos restaurantes")
public class ItemCardapioController {

    private final ItemCardapioService itemCardapioService;

    public ItemCardapioController(ItemCardapioService itemCardapioService) {
        this.itemCardapioService = itemCardapioService;
    }

    @Operation(summary = "Buscar item de cardápio por ID", description = "Retorna os dados completos de um item de cardápio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemCardapioResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ItemCardapioResponse> buscarPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(this.itemCardapioService.findById(id));
    }

    @Operation(summary = "Listar todos os itens de cardápio", description = "Retorna a lista com todos os itens de cardápio cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemCardapioResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping
    public ResponseEntity<List<ItemCardapioResponse>> buscarTodos() {
        return ResponseEntity.ok(this.itemCardapioService.findAll());
    }

    @Operation(summary = "Criar novo item de cardápio", description = "Cria um novo registro de item de cardápio no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemCardapioResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    public ResponseEntity<ItemCardapioResponse> cadastrar(@RequestBody @Valid ItemCardapioRequest itemCardapioRequest) {
        ItemCardapioResponse itemSalvo = this.itemCardapioService.save(itemCardapioRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(itemSalvo.id())
                .toUri();
        return ResponseEntity.created(location).body(itemSalvo);
    }

    @Operation(summary = "Atualizar item de cardápio", description = "Atualiza os dados de um item de cardápio existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ItemCardapioResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ItemCardapioResponse> atualizar(@PathVariable("id") Long id, @RequestBody @Valid ItemCardapioUpdateRequest itemCardapioUpdateRequest) {
        return ResponseEntity.ok(this.itemCardapioService.update(id, itemCardapioUpdateRequest));
    }

    @Operation(summary = "Deletar item de cardápio", description = "Remove um item de cardápio do sistema com base em seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable("id") Long id) {
        this.itemCardapioService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
