package br.com.fiap.challenge.application.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.challenge.application.exception.BusinessException;
import br.com.fiap.challenge.application.exception.ErrorCode;
import br.com.fiap.challenge.application.port.TipoUsuarioRepositoryPort;
import br.com.fiap.challenge.application.port.UsuarioRepositoryPort;
import br.com.fiap.challenge.domain.entities.TipoUsuario;
import br.com.fiap.challenge.domain.entities.Usuario;
import br.com.fiap.challenge.interfaces.dto.request.EnderecoRequestDTO;
import br.com.fiap.challenge.interfaces.dto.request.UsuarioCreateRequestDTO;
import br.com.fiap.challenge.interfaces.dto.request.UsuarioUpdateRequestDTO;
import br.com.fiap.challenge.interfaces.dto.request.UsuarioUpdateSenhaDTO;
import br.com.fiap.challenge.interfaces.dto.response.TipoUsuarioResponseDTO;
import br.com.fiap.challenge.interfaces.dto.response.UsuarioResponseDTO;
import br.com.fiap.challenge.interfaces.mapper.EnderecoMapper;
import br.com.fiap.challenge.interfaces.mapper.UsuarioMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {


    @Mock
    private UsuarioRepositoryPort usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private EnderecoMapper enderecoMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TipoUsuarioRepositoryPort tipoUsuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;


    private Usuario usuario;
    private UsuarioResponseDTO usuarioResponseDTO;
    private TipoUsuario tipoUsuario;
    private EnderecoRequestDTO enderecoRequestDTO;

    @BeforeEach
    void setUp() {

        tipoUsuario = new TipoUsuario();
        tipoUsuario.setId(1L);
        tipoUsuario.setDescricao("CLIENTE");

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Joao Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joaosilva");
        usuario.setSenha("senha_criptografada");
        usuario.setTipoUsuario(tipoUsuario);
        usuario.setEnderecos(new ArrayList<>());

        enderecoRequestDTO = new EnderecoRequestDTO(
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 42",
                "01310100",
                "Sao Paulo",
                "SP"
        );

        TipoUsuarioResponseDTO tipoUsuarioResponseDTO = new TipoUsuarioResponseDTO(
                1L,
                "CLIENTE"
        );

        usuarioResponseDTO = new UsuarioResponseDTO(
                1L,
                "Joao Silva",
                "joao@email.com",
                tipoUsuarioResponseDTO,
                List.of()
        );
    }



    @Test
    @DisplayName("Deve retornar DTO quando usuario e encontrado por ID")
    void findById_deveRetornarDTO_quandoUsuarioExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toResponseDTO(usuario)).thenReturn(usuarioResponseDTO);

        UsuarioResponseDTO resultado = usuarioService.findById(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Joao Silva", resultado.nome());
        assertEquals("joao@email.com", resultado.email());
        assertNotNull(resultado.tipoUsuario());
        assertEquals("CLIENTE", resultado.tipoUsuario().descricao());

        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lancar BusinessException quando usuario nao e encontrado por ID")
    void findById_deveLancarException_quandoUsuarioNaoExiste() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> usuarioService.findById(999L)
        );

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());

        verify(usuarioMapper, never()).toResponseDTO(any());
    }



    @Test
    @DisplayName("Deve retornar lista de DTOs quando existem usuarios")
    void findAll_deveRetornarListaDeDTOs_quandoExistemUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));
        when(usuarioMapper.toResponseDTO(usuario)).thenReturn(usuarioResponseDTO);

        List<UsuarioResponseDTO> resultado = usuarioService.findAll();

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Joao Silva", resultado.get(0).nome());
        assertEquals("joao@email.com", resultado.get(0).email());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nao existem usuarios")
    void findAll_deveRetornarListaVazia_quandoNaoExistemUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of());

        List<UsuarioResponseDTO> resultado = usuarioService.findAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }



    @Test
    @DisplayName("Deve retornar usuarios que correspondem ao nome buscado")
    void findByNome_deveRetornarUsuarios_quandoNomeExiste() {
        when(usuarioRepository.findByNomeContainingIgnoreCase("Joao")).thenReturn(List.of(usuario));
        when(usuarioMapper.toResponseDTO(usuario)).thenReturn(usuarioResponseDTO);

        List<UsuarioResponseDTO> resultado = usuarioService.findByNome("Joao");

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Joao Silva", resultado.get(0).nome());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nenhum nome corresponde")
    void findByNome_deveRetornarListaVazia_quandoNomeNaoExiste() {
        when(usuarioRepository.findByNomeContainingIgnoreCase("NomeInexistente"))
                .thenReturn(List.of());

        List<UsuarioResponseDTO> resultado = usuarioService.findByNome("NomeInexistente");

        assertTrue(resultado.isEmpty());
    }



    @Test
    @DisplayName("Deve salvar e retornar DTO quando dados sao validos")
    void save_deveSalvarERetornarDTO_quandoDadosValidos() {
        UsuarioCreateRequestDTO requestDTO = new UsuarioCreateRequestDTO(
                "Joao Silva",
                "joao@email.com",
                "joaosilva",
                "senha123",
                1L,
                List.of(enderecoRequestDTO)
        );

        when(usuarioRepository.findByEmailIgnoreCase("joao@email.com")).thenReturn(Optional.empty());
        when(usuarioRepository.findByLoginIgnoreCase("joaosilva")).thenReturn(Optional.empty());
        when(tipoUsuarioRepository.findById(1L)).thenReturn(Optional.of(tipoUsuario));
        when(usuarioMapper.toEntity(requestDTO)).thenReturn(usuario);
        when(passwordEncoder.encode("senha123")).thenReturn("senha_criptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioMapper.toResponseDTO(usuario)).thenReturn(usuarioResponseDTO);

        UsuarioResponseDTO resultado = usuarioService.save(requestDTO);

        assertNotNull(resultado);
        assertEquals("Joao Silva", resultado.nome());
        assertEquals("joao@email.com", resultado.email());

        verify(passwordEncoder, times(1)).encode("senha123");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lancar excecao quando e-mail ja esta em uso ao salvar")
    void save_deveLancarException_quandoEmailJaExiste() {
        UsuarioCreateRequestDTO requestDTO = new UsuarioCreateRequestDTO(
                "Outro Usuario",
                "joao@email.com",  
                "outrousuario",
                "senha123",
                1L,
                List.of(enderecoRequestDTO)
        );

        when(usuarioRepository.findByEmailIgnoreCase("joao@email.com"))
                .thenReturn(Optional.of(usuario));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> usuarioService.save(requestDTO)
        );

        assertEquals(ErrorCode.EMAIL_ALREADY_EXISTS, exception.getErrorCode());

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lancar excecao quando login ja esta em uso ao salvar")
    void save_deveLancarException_quandoLoginJaExiste() {
        UsuarioCreateRequestDTO requestDTO = new UsuarioCreateRequestDTO(
                "Outro Usuario",
                "outro@email.com",
                "joaosilva",       
                "senha123",
                1L,
                List.of(enderecoRequestDTO)
        );

        when(usuarioRepository.findByEmailIgnoreCase("outro@email.com")).thenReturn(Optional.empty());
        when(usuarioRepository.findByLoginIgnoreCase("joaosilva"))
                .thenReturn(Optional.of(usuario));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> usuarioService.save(requestDTO)
        );

        assertEquals(ErrorCode.LOGIN_ALREADY_EXISTS, exception.getErrorCode());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lancar excecao quando tipoUsuario nao existe ao salvar")
    void save_deveLancarException_quandoTipoUsuarioNaoExiste() {
        UsuarioCreateRequestDTO requestDTO = new UsuarioCreateRequestDTO(
                "Joao Silva",
                "joao@email.com",
                "joaosilva",
                "senha123",
                99L,
                List.of(enderecoRequestDTO)
        );

        when(usuarioRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.findByLoginIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(tipoUsuarioRepository.findById(99L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> usuarioService.save(requestDTO)
        );

        assertEquals(ErrorCode.USER_TYPE_NOT_FOUND, exception.getErrorCode());
        verify(usuarioRepository, never()).save(any());
    }



    @Test
    @DisplayName("Deve atualizar e retornar DTO quando dados sao validos")
    void update_deveAtualizarERetornarDTO_quandoDadosValidos() {
        UsuarioUpdateRequestDTO updateDTO = new UsuarioUpdateRequestDTO(
                "Joao Atualizado",
                "joao@email.com",
                "joaosilva",
                1L,
                List.of(enderecoRequestDTO)
        );

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findByEmailIgnoreCase("joao@email.com"))
                .thenReturn(Optional.of(usuario));
        when(tipoUsuarioRepository.findById(1L)).thenReturn(Optional.of(tipoUsuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioMapper.toResponseDTO(usuario)).thenReturn(usuarioResponseDTO);
        when(enderecoMapper.toEnderecoUsuario(enderecoRequestDTO)).thenReturn(new br.com.fiap.challenge.domain.entities.EnderecoUsuario());

        UsuarioResponseDTO resultado = usuarioService.update(updateDTO, 1L);

        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        verify(usuarioMapper, times(1)).updateEntityFromDTO(eq(updateDTO), any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lancar excecao quando usuario nao existe ao atualizar")
    void update_deveLancarException_quandoUsuarioNaoExiste() {
        UsuarioUpdateRequestDTO updateDTO = new UsuarioUpdateRequestDTO(
                "Qualquer Nome",
                "qualquer@email.com",
                "qualquerlogin",
                1L,
                List.of(enderecoRequestDTO)
        );

        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> usuarioService.update(updateDTO, 999L)
        );

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lancar excecao quando e-mail pertence a outro usuario ao atualizar")
    void update_deveLancarException_quandoEmailPertenceAOutroUsuario() {
        Usuario outroUsuario = new Usuario();
        outroUsuario.setId(2L);
        outroUsuario.setEmail("joao@email.com");

        UsuarioUpdateRequestDTO updateDTO = new UsuarioUpdateRequestDTO(
                "Joao Atualizado",
                "joao@email.com",  
                "joaosilva",
                1L,
                List.of(enderecoRequestDTO)
        );

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findByEmailIgnoreCase("joao@email.com"))
                .thenReturn(Optional.of(outroUsuario));

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> usuarioService.update(updateDTO, 1L)
        );

        assertEquals(ErrorCode.EMAIL_ALREADY_EXISTS, exception.getErrorCode());
        verify(usuarioRepository, never()).save(any());
    }



    @Test
    @DisplayName("Deve deletar usuario quando ele existe")
    void delete_deveDeletar_quandoUsuarioExiste() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).deleteById(1L);

        assertDoesNotThrow(() -> usuarioService.delete(1L));

        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lancar excecao quando usuario nao existe ao deletar")
    void delete_deveLancarException_quandoUsuarioNaoExiste() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> usuarioService.delete(999L)
        );

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());

        verify(usuarioRepository, never()).deleteById(any());
    }



    @Test
    @DisplayName("Deve atualizar senha quando senha atual esta correta")
    void updateSenha_deveAtualizar_quandoSenhaAtualCorreta() {
        UsuarioUpdateSenhaDTO senhaDTO = new UsuarioUpdateSenhaDTO(
                "senhaAtual123",
                "novaSenha456"
        );

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senhaAtual123", "senha_criptografada")).thenReturn(true);
        when(passwordEncoder.encode("novaSenha456")).thenReturn("novo_hash");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        assertDoesNotThrow(() -> usuarioService.updateSenha(senhaDTO, 1L));

        verify(passwordEncoder, times(1)).encode("novaSenha456");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lancar excecao quando senha atual esta incorreta")
    void updateSenha_deveLancarException_quandoSenhaAtualIncorreta() {
        UsuarioUpdateSenhaDTO senhaDTO = new UsuarioUpdateSenhaDTO(
                "senhaErrada",
                "novaSenha456"
        );

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senhaErrada", "senha_criptografada")).thenReturn(false);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> usuarioService.updateSenha(senhaDTO, 1L)
        );

        assertEquals(ErrorCode.INVALID_PASSWORD, exception.getErrorCode());

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lancar excecao quando usuario nao existe ao atualizar senha")
    void updateSenha_deveLancarException_quandoUsuarioNaoExiste() {
        UsuarioUpdateSenhaDTO senhaDTO = new UsuarioUpdateSenhaDTO(
                "senhaAtual123",
                "novaSenha456"
        );

        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> usuarioService.updateSenha(senhaDTO, 999L)
        );

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());

        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(usuarioRepository, never()).save(any());
    }
}