package br.com.koch.servico.cliente;

import br.com.koch.dto.cliente.PainelEnderecoDto;
import br.com.koch.dto.cliente.PainelUsuarioDto;
import br.com.koch.modelo.cliente.Cliente;
import br.com.koch.modelo.cliente.UsuarioCliente;
import br.com.koch.repositorio.cliente.RepositorioCliente;
import br.com.koch.repositorio.cliente.RepositorioUsuarioCliente;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * CT-20 — Validar atualização de perfil, segurança de e-mail e reautenticação em ServicoPainelCliente
 * Tipo   : Unitário
 * Fase   : Verificação
 * Resp.  : Igor Farias
 */
@ExtendWith(MockitoExtension.class)
class ServicoPainelClienteTest {

    @Mock private RepositorioCliente        repositorioCliente;
    @Mock private RepositorioUsuarioCliente  repositorioUsuarioCliente;
    @Mock private ServicoDetalhesCliente     servicoDetalhesCliente;

    @InjectMocks
    private ServicoPainelCliente servicoPainelCliente;

    @AfterEach
    void limparSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    // ── helpers ──────────────────────────────────────────────────────── //

    private Cliente criarCliente(int clienteId, int usuarioId, String email) {
        UsuarioCliente usuario = new UsuarioCliente();
        usuario.setId(usuarioId);
        usuario.setEmail(email);
        usuario.setSenha("$2a$10$hash");

        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        cliente.setNome("Ana Silva");
        cliente.setTelefone("4599999999");
        cliente.setEndereco("Rua Antiga, 1");
        cliente.setUsuario(usuario);
        return cliente;
    }

    private PainelUsuarioDto criarDto(String nome, String telefone, String email) {
        PainelUsuarioDto dto = new PainelUsuarioDto();
        dto.setNome(nome);
        dto.setTelefone(telefone);
        dto.setEmail(email);
        return dto;
    }

    // ------------------------------------------------------------------ //
    // Cenário A — atualizar dados com alteração de e-mail                 //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioA_atualizarDados_devePersistirEReautenticarQuandoEmailAlterado() {
        // Arrange
        Cliente cliente = criarCliente(1, 1, "ana@email.com");
        PainelUsuarioDto dto = criarDto("Ana S.", "4599999", "novo_ana@email.com");

        UserDetails novoUserDetails = new User(
                "novo_ana@email.com", "$2a$10$hash", List.of()
        );

        when(repositorioCliente.findByUsuario_Email("ana@email.com"))
                .thenReturn(Optional.of(cliente));
        when(repositorioUsuarioCliente.findByEmail("novo_ana@email.com"))
                .thenReturn(Optional.empty());
        when(servicoDetalhesCliente.loadUserByUsername("novo_ana@email.com"))
                .thenReturn(novoUserDetails);

        // Act
        servicoPainelCliente.atualizarDados("ana@email.com", dto);

        // Assert — persistência
        verify(repositorioUsuarioCliente, times(1)).save(argThat(u ->
                "novo_ana@email.com".equals(u.getEmail())
        ));
        verify(repositorioCliente, times(1)).save(argThat(c ->
                "Ana S.".equals(c.getNome()) && "4599999".equals(c.getTelefone())
        ));

        // Assert — reautenticação no SecurityContextHolder
        assertNotNull(SecurityContextHolder.getContext().getAuthentication(),
                "O SecurityContextHolder deve ter autenticação após alteração de e-mail");
        assertEquals("novo_ana@email.com",
                SecurityContextHolder.getContext().getAuthentication().getName());
    }

    // ------------------------------------------------------------------ //
    // Cenário B — campos obrigatórios vazios                              //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioB_atualizarDados_deveLancarExcecaoQuandoCamposObrigatoriosVazios() {
        // Arrange
        Cliente cliente = criarCliente(1, 1, "ana@email.com");
        PainelUsuarioDto dto = criarDto("", " ", null);

        when(repositorioCliente.findByUsuario_Email("ana@email.com"))
                .thenReturn(Optional.of(cliente));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> servicoPainelCliente.atualizarDados("ana@email.com", dto)
        );
        assertEquals("Nome, telefone e e-mail são obrigatórios.", ex.getMessage());
        verify(repositorioCliente, never()).save(any());
    }

    // ------------------------------------------------------------------ //
    // Cenário C — conflito de e-mail duplicado                            //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioC_atualizarDados_deveLancarExcecaoQuandoEmailPertenceAOutraConta() {
        // Arrange — usuário logado tem ID 1; e-mail novo pertence ao usuário ID 2
        Cliente clienteLogado = criarCliente(1, 1, "ana@email.com");
        PainelUsuarioDto dto = criarDto("Ana S.", "4599999", "outro@email.com");

        UsuarioCliente outraConta = new UsuarioCliente();
        outraConta.setId(2);
        outraConta.setEmail("outro@email.com");

        when(repositorioCliente.findByUsuario_Email("ana@email.com"))
                .thenReturn(Optional.of(clienteLogado));
        when(repositorioUsuarioCliente.findByEmail("outro@email.com"))
                .thenReturn(Optional.of(outraConta));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> servicoPainelCliente.atualizarDados("ana@email.com", dto)
        );
        assertEquals("Este e-mail já está em uso por outra conta.", ex.getMessage());
        verify(repositorioUsuarioCliente, never()).save(any());
    }

    // ------------------------------------------------------------------ //
    // Cenário D — atualização de endereço                                 //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioD_atualizarEndereco_devePersistirEnderecoValido() {
        // Arrange
        Cliente cliente = criarCliente(1, 1, "ana@email.com");
        PainelEnderecoDto dto = new PainelEnderecoDto();
        dto.setEndereco("Rua Nova, 123");

        when(repositorioCliente.findByUsuario_Email("ana@email.com"))
                .thenReturn(Optional.of(cliente));

        // Act
        servicoPainelCliente.atualizarEndereco("ana@email.com", dto);

        // Assert
        verify(repositorioCliente, times(1)).save(argThat(c ->
                "Rua Nova, 123".equals(c.getEndereco())
        ));
    }

    @Test
    void cenarioD_atualizarEndereco_deveLancarExcecaoQuandoEnderecoVazio() {
        // Arrange
        Cliente cliente = criarCliente(1, 1, "ana@email.com");
        PainelEnderecoDto dto = new PainelEnderecoDto();
        dto.setEndereco(" ");

        when(repositorioCliente.findByUsuario_Email("ana@email.com"))
                .thenReturn(Optional.of(cliente));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> servicoPainelCliente.atualizarEndereco("ana@email.com", dto)
        );
        assertEquals("O endereço não pode ficar vazio.", ex.getMessage());
        verify(repositorioCliente, never()).save(any());
    }
}