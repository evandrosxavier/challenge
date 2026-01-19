package br.com.fiap.challenge.controller;

import br.com.fiap.challenge.dto.request.UsuarioCreateRequestDTO;
import br.com.fiap.challenge.dto.request.UsuarioUpdateRequestDTO;
import br.com.fiap.challenge.dto.request.UsuarioUpdateSenhaDTO;
import br.com.fiap.challenge.dto.response.UsuarioResponseDTO;
import br.com.fiap.challenge.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios" )
@Tag(name = "Usuários", description = "Endpoints para o gerenciamento de usuários do sistema MyDelivery.")
public class UsuarioController {

    private final UsuarioService usuarioService;

    // Injeção de dependência via construtor (melhor prática)
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID", description = "Retorna os detalhes de um usuário específico com base em seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado para o ID informado")
    })
    public ResponseEntity<UsuarioResponseDTO> findById(@PathVariable Long id) {
        UsuarioResponseDTO usuarioResponseDTO = this.usuarioService.findById(id);
        return ResponseEntity.ok(usuarioResponseDTO);
    }

    @GetMapping
    @Operation(summary = "Listar usuários", description = "Retorna uma lista de todos os usuários ou filtra pelo nome.")
    @ApiResponse(responseCode = "200", description = "Listagem realizada com sucesso")
    public ResponseEntity<List<UsuarioResponseDTO>> find(
            @RequestParam(value = "nome", required = false) String nome) {
        List<UsuarioResponseDTO> listDTO;

        if (nome != null && !nome.isBlank()) {
            listDTO = this.usuarioService.findByNome(nome);
        } else {
            listDTO = this.usuarioService.findAll();
        }
        return ResponseEntity.ok(listDTO);
    }

    @PostMapping
    @Operation(summary = "Criar novo usuário", description = "Cria um novo registro de usuário no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos (campos faltando, e-mail já em uso, etc.)")
    })
    public ResponseEntity<UsuarioResponseDTO> save(@RequestBody @Valid UsuarioCreateRequestDTO usuarioCreateRequestDTO) {

        UsuarioResponseDTO usuarioSalvo = this.usuarioService.save(usuarioCreateRequestDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(usuarioSalvo.id())
                .toUri();

        return ResponseEntity.created(location).body(usuarioSalvo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente (exceto senha).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado para o ID informado"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos")
    })
    public ResponseEntity<UsuarioResponseDTO> update(@PathVariable Long id, @RequestBody @Valid UsuarioUpdateRequestDTO usuarioUpdateRequestDTO) {
        UsuarioResponseDTO usuarioResponseDTO = this.usuarioService.update(usuarioUpdateRequestDTO, id);
        return ResponseEntity.ok(usuarioResponseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir usuário", description = "Remove um usuário do sistema com base em seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado para o ID informado")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/senha") // URL mais descritiva para a ação
    @Operation(summary = "Atualizar senha do usuário", description = "Permite que um usuário atualize sua própria senha.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Senha atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado para o ID informado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (ex: senha atual incorreta)")
    })
    public ResponseEntity<Void> updateSenha(@PathVariable Long id, @RequestBody @Valid UsuarioUpdateSenhaDTO usuarioUpdateSenhaDTO) {
        this.usuarioService.updateSenha(usuarioUpdateSenhaDTO, id);
        return ResponseEntity.noContent().build();
    }
}
