package br.com.fiap.challenge.application.service;

// ============================================================
// IMPORTS
// ============================================================

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


// @ExtendWith(MockitoExtension.class) — ativa o Mockito para esta classe.
// Sem isso, os @Mock e @InjectMocks nao funcionam.
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    // ============================================================
    // MOCKS — "dubles" das dependencias reais.
    // O Mockito cria objetos falsos que voce controla.
    // O banco de dados NUNCA e chamado de verdade.
    // ============================================================

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

    // @InjectMocks — cria a instancia REAL do UsuarioService
    // e injeta automaticamente todos os @Mock acima nela.
    // E o unico objeto "de verdade" no teste.
    @InjectMocks
    private UsuarioService usuarioService;

    // ============================================================
    // OBJETOS DE APOIO — reutilizados por varios testes
    // ============================================================

    private Usuario usuario;
    private UsuarioResponseDTO usuarioResponseDTO;
    private TipoUsuario tipoUsuario;
    private EnderecoRequestDTO enderecoRequestDTO;

    // @BeforeEach — roda ANTES DE CADA teste.
    // Monta os objetos base para nao repetir a construcao em cada metodo.
    @BeforeEach
    void setUp() {

        // --- Entidade TipoUsuario ---
        // ATENCAO: ajuste o setter conforme o campo real da sua entidade.
        // Pode ser setDescricao() ou setNome() dependendo do seu codigo.
        tipoUsuario = new TipoUsuario();
        tipoUsuario.setId(1L);
        tipoUsuario.setDescricao("CLIENTE");

        // --- Entidade Usuario ---
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Joao Silva");
        usuario.setEmail("joao@email.com");
        usuario.setLogin("joaosilva");
        usuario.setSenha("senha_criptografada");
        usuario.setTipoUsuario(tipoUsuario);
        usuario.setEnderecos(new ArrayList<>());

        // --- EnderecoRequestDTO ---
        // Campos: logradouro, numero, bairro, complemento, cep, cidade, estado
        // Ajuste a ordem dos parametros conforme a declaracao do seu record
        enderecoRequestDTO = new EnderecoRequestDTO(
                "Avenida Paulista",
                "1000",
                "Bela Vista",
                "Apto 42",
                "01310100",
                "Sao Paulo",
                "SP"
        );

        // --- TipoUsuarioResponseDTO: record com (id, descricao) ---
        TipoUsuarioResponseDTO tipoUsuarioResponseDTO = new TipoUsuarioResponseDTO(
                1L,
                "CLIENTE"
        );

        // --- UsuarioResponseDTO: record com (id, nome, email, tipoUsuario, enderecos) ---
        // Nao ha campo "login" neste DTO conforme a declaracao do seu record
        usuarioResponseDTO = new UsuarioResponseDTO(
                1L,
                "Joao Silva",
                "joao@email.com",
                tipoUsuarioResponseDTO,
                List.of()
        );
    }


    // ============================================================
    //  TESTES DE findById
    // ============================================================

    @Test
    @DisplayName("Deve retornar DTO quando usuario e encontrado por ID")
    void findById_deveRetornarDTO_quandoUsuarioExiste() {
        // ARRANGE
        // "quando o repositorio buscar o id 1, retorne nosso usuario"
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        // "quando o mapper converter o usuario, retorne o DTO"
        when(usuarioMapper.toResponseDTO(usuario)).thenReturn(usuarioResponseDTO);

        // ACT
        UsuarioResponseDTO resultado = usuarioService.findById(1L);

        // ASSERT
        assertNotNull(resultado);
        assertEquals(1L, resultado.id());
        assertEquals("Joao Silva", resultado.nome());
        assertEquals("joao@email.com", resultado.email());
        assertNotNull(resultado.tipoUsuario());
        assertEquals("CLIENTE", resultado.tipoUsuario().descricao());

        // Repositorio foi chamado exatamente 1 vez com o id correto
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lancar BusinessException quando usuario nao e encontrado por ID")
    void findById_deveLancarException_quandoUsuarioNaoExiste() {
        // ARRANGE
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        // ACT + ASSERT
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> usuarioService.findById(999L)
        );

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());

        // Mapper NUNCA deve ser chamado se o usuario nao foi encontrado
        verify(usuarioMapper, never()).toResponseDTO(any());
    }


    // ============================================================
    //  TESTES DE findAll
    // ============================================================

    @Test
    @DisplayName("Deve retornar lista de DTOs quando existem usuarios")
    void findAll_deveRetornarListaDeDTOs_quandoExistemUsuarios() {
        // ARRANGE
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));
        when(usuarioMapper.toResponseDTO(usuario)).thenReturn(usuarioResponseDTO);

        // ACT
        List<UsuarioResponseDTO> resultado = usuarioService.findAll();

        // ASSERT
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Joao Silva", resultado.get(0).nome());
        assertEquals("joao@email.com", resultado.get(0).email());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nao existem usuarios")
    void findAll_deveRetornarListaVazia_quandoNaoExistemUsuarios() {
        // ARRANGE
        when(usuarioRepository.findAll()).thenReturn(List.of());

        // ACT
        List<UsuarioResponseDTO> resultado = usuarioService.findAll();

        // ASSERT
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }


    // ============================================================
    //  TESTES DE findByNome
    // ============================================================

    @Test
    @DisplayName("Deve retornar usuarios que correspondem ao nome buscado")
    void findByNome_deveRetornarUsuarios_quandoNomeExiste() {
        // ARRANGE
        when(usuarioRepository.findByNomeContainingIgnoreCase("Joao")).thenReturn(List.of(usuario));
        when(usuarioMapper.toResponseDTO(usuario)).thenReturn(usuarioResponseDTO);

        // ACT
        List<UsuarioResponseDTO> resultado = usuarioService.findByNome("Joao");

        // ASSERT
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Joao Silva", resultado.get(0).nome());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nenhum nome corresponde")
    void findByNome_deveRetornarListaVazia_quandoNomeNaoExiste() {
        // ARRANGE
        when(usuarioRepository.findByNomeContainingIgnoreCase("NomeInexistente"))
                .thenReturn(List.of());

        // ACT
        List<UsuarioResponseDTO> resultado = usuarioService.findByNome("NomeInexistente");

        // ASSERT
        assertTrue(resultado.isEmpty());
    }


    // ============================================================
    //  TESTES DE save
    // ============================================================

    @Test
    @DisplayName("Deve salvar e retornar DTO quando dados sao validos")
    void save_deveSalvarERetornarDTO_quandoDadosValidos() {
        // ARRANGE
        // UsuarioCreateRequestDTO: record com (nome, email, login, senha, tipoUsuario, enderecos)
        UsuarioCreateRequestDTO requestDTO = new UsuarioCreateRequestDTO(
                "Joao Silva",
                "joao@email.com",
                "joaosilva",
                "senha123",
                1L,
                List.of(enderecoRequestDTO)
        );

        // Simula: email nao existe, login nao existe, tipoUsuario existe
        when(usuarioRepository.findByEmailIgnoreCase("joao@email.com")).thenReturn(Optional.empty());
        when(usuarioRepository.findByLoginIgnoreCase("joaosilva")).thenReturn(Optional.empty());
        when(tipoUsuarioRepository.findById(1L)).thenReturn(Optional.of(tipoUsuario));
        when(usuarioMapper.toEntity(requestDTO)).thenReturn(usuario);
        when(passwordEncoder.encode("senha123")).thenReturn("senha_criptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioMapper.toResponseDTO(usuario)).thenReturn(usuarioResponseDTO);

        // ACT
        UsuarioResponseDTO resultado = usuarioService.save(requestDTO);

        // ASSERT
        assertNotNull(resultado);
        assertEquals("Joao Silva", resultado.nome());
        assertEquals("joao@email.com", resultado.email());

        // Senha foi criptografada antes de salvar
        verify(passwordEncoder, times(1)).encode("senha123");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lancar excecao quando e-mail ja esta em uso ao salvar")
    void save_deveLancarException_quandoEmailJaExiste() {
        // ARRANGE
        UsuarioCreateRequestDTO requestDTO = new UsuarioCreateRequestDTO(
                "Outro Usuario",
                "joao@email.com",   // e-mail ja cadastrado
                "outrousuario",
                "senha123",
                1L,
                List.of(enderecoRequestDTO)
        );

        // Simula: e-mail JA existe no banco
        when(usuarioRepository.findByEmailIgnoreCase("joao@email.com"))
                .thenReturn(Optional.of(usuario));

        // ACT + ASSERT
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> usuarioService.save(requestDTO)
        );

        assertEquals(ErrorCode.EMAIL_ALREADY_EXISTS, exception.getErrorCode());

        // save NUNCA deve ser chamado — o fluxo parou antes
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lancar excecao quando login ja esta em uso ao salvar")
    void save_deveLancarException_quandoLoginJaExiste() {
        // ARRANGE
        UsuarioCreateRequestDTO requestDTO = new UsuarioCreateRequestDTO(
                "Outro Usuario",
                "outro@email.com",
                "joaosilva",        // login ja cadastrado
                "senha123",
                1L,
                List.of(enderecoRequestDTO)
        );

        when(usuarioRepository.findByEmailIgnoreCase("outro@email.com")).thenReturn(Optional.empty());
        when(usuarioRepository.findByLoginIgnoreCase("joaosilva"))
                .thenReturn(Optional.of(usuario));

        // ACT + ASSERT
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
        // ARRANGE
        UsuarioCreateRequestDTO requestDTO = new UsuarioCreateRequestDTO(
                "Joao Silva",
                "joao@email.com",
                "joaosilva",
                "senha123",
                99L,                // tipoUsuario inexistente
                List.of(enderecoRequestDTO)
        );

        when(usuarioRepository.findByEmailIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(usuarioRepository.findByLoginIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(tipoUsuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // ACT + ASSERT
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> usuarioService.save(requestDTO)
        );

        assertEquals(ErrorCode.USER_TYPE_NOT_FOUND, exception.getErrorCode());
        verify(usuarioRepository, never()).save(any());
    }


    // ============================================================
    //  TESTES DE update
    // ============================================================

    @Test
    @DisplayName("Deve atualizar e retornar DTO quando dados sao validos")
    void update_deveAtualizarERetornarDTO_quandoDadosValidos() {
        // ARRANGE
        // Ajuste os campos conforme a declaracao do seu UsuarioUpdateRequestDTO
        UsuarioUpdateRequestDTO updateDTO = new UsuarioUpdateRequestDTO(
                "Joao Atualizado",
                "joao@email.com",
                "joaosilva",
                1L,
                List.of(enderecoRequestDTO)
        );

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        // Retorna o PROPRIO usuario (mesmo id) — sem conflito de e-mail
        when(usuarioRepository.findByEmailIgnoreCase("joao@email.com"))
                .thenReturn(Optional.of(usuario));
        when(tipoUsuarioRepository.findById(1L)).thenReturn(Optional.of(tipoUsuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioMapper.toResponseDTO(usuario)).thenReturn(usuarioResponseDTO);
        when(enderecoMapper.toEnderecoUsuario(enderecoRequestDTO)).thenReturn(new br.com.fiap.challenge.domain.entities.EnderecoUsuario());

        // ACT
        UsuarioResponseDTO resultado = usuarioService.update(updateDTO, 1L);

        // ASSERT
        assertNotNull(resultado);
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
        // updateEntityFromDTO copia os campos do DTO para a entidade existente
        verify(usuarioMapper, times(1)).updateEntityFromDTO(eq(updateDTO), any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lancar excecao quando usuario nao existe ao atualizar")
    void update_deveLancarException_quandoUsuarioNaoExiste() {
        // ARRANGE
        UsuarioUpdateRequestDTO updateDTO = new UsuarioUpdateRequestDTO(
                "Qualquer Nome",
                "qualquer@email.com",
                "qualquerlogin",
                1L,
                List.of(enderecoRequestDTO)
        );

        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        // ACT + ASSERT
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
        // ARRANGE — segundo usuario com ID diferente simula o conflito
        Usuario outroUsuario = new Usuario();
        outroUsuario.setId(2L);
        outroUsuario.setEmail("joao@email.com");

        UsuarioUpdateRequestDTO updateDTO = new UsuarioUpdateRequestDTO(
                "Joao Atualizado",
                "joao@email.com",   // e-mail que ja pertence ao usuario de id=2
                "joaosilva",
                1L,
                List.of(enderecoRequestDTO)
        );

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        // Retorna OUTRO usuario — o filter(!u.getId().equals(id)) dispara a excecao
        when(usuarioRepository.findByEmailIgnoreCase("joao@email.com"))
                .thenReturn(Optional.of(outroUsuario));

        // ACT + ASSERT
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> usuarioService.update(updateDTO, 1L)
        );

        assertEquals(ErrorCode.EMAIL_ALREADY_EXISTS, exception.getErrorCode());
        verify(usuarioRepository, never()).save(any());
    }


    // ============================================================
    //  TESTES DE delete
    // ============================================================

    @Test
    @DisplayName("Deve deletar usuario quando ele existe")
    void delete_deveDeletar_quandoUsuarioExiste() {
        // ARRANGE
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).deleteById(1L);

        // ACT — delete e void, verificamos apenas que nao lancou excecao
        assertDoesNotThrow(() -> usuarioService.delete(1L));

        // ASSERT — deleteById foi chamado com o id correto
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lancar excecao quando usuario nao existe ao deletar")
    void delete_deveLancarException_quandoUsuarioNaoExiste() {
        // ARRANGE
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        // ACT + ASSERT
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> usuarioService.delete(999L)
        );

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());

        // deleteById NUNCA deve ser chamado
        verify(usuarioRepository, never()).deleteById(any());
    }


    // ============================================================
    //  TESTES DE updateSenha
    // ============================================================

    @Test
    @DisplayName("Deve atualizar senha quando senha atual esta correta")
    void updateSenha_deveAtualizar_quandoSenhaAtualCorreta() {
        // ARRANGE
        // Ajuste os campos conforme a declaracao do seu UsuarioUpdateSenhaDTO
        UsuarioUpdateSenhaDTO senhaDTO = new UsuarioUpdateSenhaDTO(
                "senhaAtual123",
                "novaSenha456"
        );

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        // A senha atual bate com o hash salvo na entidade
        when(passwordEncoder.matches("senhaAtual123", "senha_criptografada")).thenReturn(true);
        when(passwordEncoder.encode("novaSenha456")).thenReturn("novo_hash");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // ACT
        assertDoesNotThrow(() -> usuarioService.updateSenha(senhaDTO, 1L));

        // ASSERT
        verify(passwordEncoder, times(1)).encode("novaSenha456");
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lancar excecao quando senha atual esta incorreta")
    void updateSenha_deveLancarException_quandoSenhaAtualIncorreta() {
        // ARRANGE
        UsuarioUpdateSenhaDTO senhaDTO = new UsuarioUpdateSenhaDTO(
                "senhaErrada",
                "novaSenha456"
        );

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        // Senha atual NAO bate com o hash
        when(passwordEncoder.matches("senhaErrada", "senha_criptografada")).thenReturn(false);

        // ACT + ASSERT
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> usuarioService.updateSenha(senhaDTO, 1L)
        );

        assertEquals(ErrorCode.INVALID_PASSWORD, exception.getErrorCode());

        // Nova senha nunca deve ser salva
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lancar excecao quando usuario nao existe ao atualizar senha")
    void updateSenha_deveLancarException_quandoUsuarioNaoExiste() {
        // ARRANGE
        UsuarioUpdateSenhaDTO senhaDTO = new UsuarioUpdateSenhaDTO(
                "senhaAtual123",
                "novaSenha456"
        );

        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        // ACT + ASSERT
        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> usuarioService.updateSenha(senhaDTO, 999L)
        );

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());

        // Nenhuma verificacao de senha e nenhum save deve ocorrer
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(usuarioRepository, never()).save(any());
    }
}