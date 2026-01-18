package br.com.fiap.challenge.controller;

import br.com.fiap.challenge.dto.request.UsuarioCreateRequestDTO;
import br.com.fiap.challenge.dto.request.UsuarioUpdateRequestDTO;
import br.com.fiap.challenge.dto.request.UsuarioUpdateSenhaDTO;
import br.com.fiap.challenge.dto.response.UsuarioResponseDTO;
import br.com.fiap.challenge.model.Usuario;
import br.com.fiap.challenge.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/usuarios")
public class UsuarioController {


    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> findById(@PathVariable Long id) {
        UsuarioResponseDTO usuarioResponseDTO = this.usuarioService.findById(id);
        return ResponseEntity.ok(usuarioResponseDTO);
    }


    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> find(
            @RequestParam(value = "nome", required = false) String nome) {
        List<UsuarioResponseDTO> listDTO;

        if (nome != null && !nome.isBlank()) {
            listDTO = this.usuarioService.findByNome(nome);
        } else {
            listDTO = this.usuarioService.findAll();
        }

        return ResponseEntity.ok().body(listDTO);

    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody UsuarioCreateRequestDTO usuarioCreateRequestDTO) {
        this.usuarioService.save(usuarioCreateRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> update(@RequestBody UsuarioUpdateRequestDTO usuarioUpdateRequestDTO, @PathVariable Long id) {
        UsuarioResponseDTO usuarioResponseDTO = this.usuarioService.update(usuarioUpdateRequestDTO, id);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioResponseDTO);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping ("/{id}")
    public ResponseEntity<Void> updateSenha (@RequestBody UsuarioUpdateSenhaDTO usuarioUpdateSenhaDTO, @PathVariable Long id) {
        this.usuarioService.updateSenha(usuarioUpdateSenhaDTO,id);
        return ResponseEntity.noContent().build();

    }

}
