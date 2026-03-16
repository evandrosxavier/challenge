package br.com.fiap.challenge.interfaces.controller;

import br.com.fiap.challenge.interfaces.dto.request.TipoUsuarioRequestDTO;
import br.com.fiap.challenge.interfaces.dto.request.TipoUsuarioUpdateDTO;
import br.com.fiap.challenge.interfaces.dto.response.TipoUsuarioResponseDTO;
import br.com.fiap.challenge.application.service.TipoUsuarioService;
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
@RequestMapping ("api/v1/tipo-usuario")
@Tag(name = "Tipo de Usuário", description = "API responsável pela gestão dos tipos de usuário")
public class TipoUsuarioController {

    private TipoUsuarioService tipoUsuarioService;

    public TipoUsuarioController (TipoUsuarioService tipoUsuarioService) {
        this.tipoUsuarioService = tipoUsuarioService;
    }

    @Operation(summary = "Busca tipo de usuário por ID", description = "Retorna os dados completos de um tipo de usuário")
    @GetMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TipoUsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })


    public ResponseEntity<TipoUsuarioResponseDTO> buscarPorID (@PathVariable ("id") Long id) {
       return  ResponseEntity.ok(this.tipoUsuarioService.findById(id));
    }



    @Operation(summary = "Busca a lista de tipos de usuário", description = "Retorna a lista com dados completos dos tipos de usuário")
    @GetMapping
    @ApiResponses (value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TipoUsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<List<TipoUsuarioResponseDTO>> buscarTodos () {
        return ResponseEntity.ok(this.tipoUsuarioService.findAll());

    }
    @Operation(summary = "Cadastra tipo de usuário", description = "Efetua o cadastro de tipos de usuário")
    @ApiResponses (value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TipoUsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping
    public ResponseEntity<TipoUsuarioResponseDTO> cadastrar (@RequestBody @Valid TipoUsuarioRequestDTO tipoUsuarioRequestDTO) {
        return ResponseEntity.ok(this.tipoUsuarioService.save(tipoUsuarioRequestDTO));
    }

    @Operation(summary = "Atualiza tipo de usuário", description = "Efetua a atualização de tipos de usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TipoUsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<TipoUsuarioResponseDTO> atualizar (@PathVariable("id") Long id, @RequestBody @Valid TipoUsuarioUpdateDTO tipoUsuarioUpdateDTO) {
        TipoUsuarioResponseDTO tipoUsuarioSalvo =this.tipoUsuarioService.update(id,tipoUsuarioUpdateDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(tipoUsuarioSalvo.id()).toUri();
        return ResponseEntity.created(location).body(tipoUsuarioSalvo);
    }

    @Operation(summary = "Deleta tipo de usuário", description = "Efetua a exclusão de tipos de usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/problem+json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar (@PathVariable ("id") Long id) {
        this.tipoUsuarioService.delete(id);
        return ResponseEntity.noContent().build();

    }
}
