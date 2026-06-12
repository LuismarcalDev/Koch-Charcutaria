package br.com.koch.servico.cliente;

import br.com.koch.modelo.cliente.UsuarioCliente;
import br.com.koch.repositorio.cliente.RepositorioUsuarioCliente;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * CT-19 — Validar carregamento de credenciais, tratamento de perfis e fallback em ServicoDetalhesCliente
 * Tipo   : Unitário
 * Fase   : Verificação
 * Resp.  : Igor Farias
 */
@ExtendWith(MockitoExtension.class)
class ServicoDetalhesClienteTest {

    @Mock
    private RepositorioUsuarioCliente repositorioUsuarioCliente;

    @InjectMocks
    private ServicoDetalhesCliente servicoDetalhesCliente;

    // ── helper ───────────────────────────────────────────────────────── //

    private UsuarioCliente criarUsuario(String email, String senha, String tipo) {
        UsuarioCliente u = new UsuarioCliente();
        u.setEmail(email);
        u.setSenha(senha);
        u.setTipo(tipo);
        return u;
    }

    // ------------------------------------------------------------------ //
    // Cenário A — tipo nulo/branco → fallback ROLE_CLIENTE                //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioA_loadUserByUsername_deveAplicarFallbackRoleClienteQuandoTipoEmBranco() {
        // Arrange
        UsuarioCliente usuario = criarUsuario("cliente@email.com", "$2a$10$hashA", "");
        when(repositorioUsuarioCliente.findByEmail("cliente@email.com"))
                .thenReturn(Optional.of(usuario));

        // Act
        UserDetails detalhes = servicoDetalhesCliente.loadUserByUsername("cliente@email.com");

        // Assert
        assertEquals("cliente@email.com", detalhes.getUsername());
        assertEquals("$2a$10$hashA", detalhes.getPassword());

        GrantedAuthority autoridade = detalhes.getAuthorities().iterator().next();
        assertEquals("ROLE_CLIENTE", autoridade.getAuthority(),
                "Tipo em branco deve resultar no perfil padrão ROLE_CLIENTE");
    }

    @Test
    void cenarioA_loadUserByUsername_deveAplicarFallbackRoleClienteQuandoTipoNulo() {
        // Arrange
        UsuarioCliente usuario = criarUsuario("cliente@email.com", "$2a$10$hashA", null);
        when(repositorioUsuarioCliente.findByEmail("cliente@email.com"))
                .thenReturn(Optional.of(usuario));

        // Act
        UserDetails detalhes = servicoDetalhesCliente.loadUserByUsername("cliente@email.com");

        // Assert
        GrantedAuthority autoridade = detalhes.getAuthorities().iterator().next();
        assertEquals("ROLE_CLIENTE", autoridade.getAuthority(),
                "Tipo nulo deve resultar no perfil padrão ROLE_CLIENTE");
    }

    // ------------------------------------------------------------------ //
    // Cenário B — tipo customizado com espaço → trim + uppercase          //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioB_loadUserByUsername_deveAplicarTrimEUpperCaseNoPerfil() {
        // Arrange — "vip " com espaço à direita deve virar "ROLE_VIP"
        UsuarioCliente usuario = criarUsuario("vip@email.com", "$2a$10$hashB", "vip ");
        when(repositorioUsuarioCliente.findByEmail("vip@email.com"))
                .thenReturn(Optional.of(usuario));

        // Act
        UserDetails detalhes = servicoDetalhesCliente.loadUserByUsername("vip@email.com");

        // Assert
        GrantedAuthority autoridade = detalhes.getAuthorities().iterator().next();
        assertEquals("ROLE_VIP", autoridade.getAuthority(),
                "Tipo 'vip ' deve ser normalizado para ROLE_VIP");
    }

    // ------------------------------------------------------------------ //
    // Cenário C — cliente inexistente                                     //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioC_loadUserByUsername_deveLancarUsernameNotFoundExceptionQuandoEmailNaoExiste() {
        // Arrange
        when(repositorioUsuarioCliente.findByEmail("nao_existe@email.com"))
                .thenReturn(Optional.empty());

        // Act + Assert
        UsernameNotFoundException ex = assertThrows(
                UsernameNotFoundException.class,
                () -> servicoDetalhesCliente.loadUserByUsername("nao_existe@email.com")
        );
        assertEquals("Cliente não encontrado.", ex.getMessage());
    }
}