package br.com.koch.servico;

import br.com.koch.modelo.admin.Perfil;
import br.com.koch.modelo.admin.Usuario;
import br.com.koch.repositorio.RepositorioUsuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * CT-23 — Validar carregamento de credenciais e mapeamento de perfis em ServicoDetalhesUsuario
 * Tipo   : Unitário
 * Fase   : Verificação
 * Resp.  : Igor Farias
 */
@ExtendWith(MockitoExtension.class)
class ServicoDetalhesUsuarioTest {

    @Mock
    private RepositorioUsuario repositorioUsuario;

    @InjectMocks
    private ServicoDetalhesUsuario servicoDetalhesUsuario;

    // ------------------------------------------------------------------ //
    // Cenário A — usuário encontrado                                      //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioA_loadUserByUsername_deveRetornarUserDetailsComCredenciaisEAutoridade() {
        // Arrange
        Usuario usuario = new Usuario(
                UUID.randomUUID(),
                "Admin Geral",
                "geral@koch.com",
                "$2a$10$hashSimulado",
                Perfil.ADMINISTRADOR
        );

        when(repositorioUsuario.buscarPorEmail("geral@koch.com"))
                .thenReturn(Optional.of(usuario));

        // Act
        UserDetails detalhes = servicoDetalhesUsuario.loadUserByUsername("geral@koch.com");

        // Assert — credenciais
        assertEquals("geral@koch.com", detalhes.getUsername(),
                "O username do UserDetails deve ser o e-mail do usuário");
        assertEquals("$2a$10$hashSimulado", detalhes.getPassword(),
                "O password do UserDetails deve ser o hash da senha");

        // Assert — autoridade com prefixo ROLE_
        assertEquals(1, detalhes.getAuthorities().size(),
                "Deve haver exatamente uma autoridade mapeada");

        GrantedAuthority autoridade = detalhes.getAuthorities().iterator().next();
        assertEquals("ROLE_ADMINISTRADOR", autoridade.getAuthority(),
                "A autoridade deve seguir o padrão ROLE_");
    }

    // ------------------------------------------------------------------ //
    // Cenário B — usuário inexistente                                     //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioB_loadUserByUsername_deveLancarUsernameNotFoundExceptionQuandoEmailNaoExiste() {
        // Arrange
        when(repositorioUsuario.buscarPorEmail("email.fantasma@koch.com"))
                .thenReturn(Optional.empty());

        // Act + Assert
        UsernameNotFoundException ex = assertThrows(
                UsernameNotFoundException.class,
                () -> servicoDetalhesUsuario.loadUserByUsername("email.fantasma@koch.com")
        );
        assertEquals("Usuário não encontrado.", ex.getMessage());
    }
}