package br.com.fiap.challenge.controller;

import br.com.fiap.challenge.dto.request.RestauranteRequest;
import br.com.fiap.challenge.dto.request.RestauranteUpdateRequest;
import br.com.fiap.challenge.dto.response.RestauranteResponse;
import br.com.fiap.challenge.service.RestauranteService;
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
@RequestMapping("/api/v1/restaurantes")
@Tag(name = "Restaurantes", description = "API responsável pela gestão de restaurantes.")
public class RestauranteController {

    private final RestauranteService restauranteService;

    public RestauranteController(RestauranteService restauranteService) {
        this.restauranteService = restauranteService;
    }

    @Operation(summary = "Buscar restaurante por ID", description = "Retorna os detalhes de um restaurante específico com base em seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestauranteResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<RestauranteResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(this.restauranteService.findById(id));
    }

    @Operation(summary = "Listar restaurantes", description = "Retorna uma lista de todos os restaurantes cadastrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping
    public ResponseEntity<List<RestauranteResponse>> findAll() {
        return ResponseEntity.ok(this.restauranteService.findAll());
    }

    @Operation(summary = "Criar novo restaurante", description = "Cria um novo registro de restaurante no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestauranteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    public ResponseEntity<RestauranteResponse> save(@RequestBody @Valid RestauranteRequest restauranteRequest) {
        RestauranteResponse restauranteSalvo = this.restauranteService.save(restauranteRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(restauranteSalvo.id()).toUri();
        return ResponseEntity.created(location).body(restauranteSalvo);
    }

    @Operation(summary = "Atualizar restaurante", description = "Atualiza os dados de um restaurante existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RestauranteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "409", description = "Conflict", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<RestauranteResponse> update(@PathVariable Long id, @RequestBody @Valid RestauranteUpdateRequest restauranteUpdateRequest) {
        return ResponseEntity.ok(this.restauranteService.update(id, restauranteUpdateRequest));
    }

    @Operation(summary = "Excluir restaurante", description = "Remove um restaurante do sistema com base em seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.restauranteService.delete(id);
        return ResponseEntity.noContent().build();
    }

}

