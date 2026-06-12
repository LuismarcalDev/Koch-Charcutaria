package br.com.koch.servico;

import br.com.koch.dto.admin.CadastroUsuarioRequest;
import br.com.koch.modelo.admin.Perfil;
import br.com.koch.modelo.admin.Usuario;
import br.com.koch.repositorio.RepositorioUsuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * CT-22 — Validar cadastro de usuário administrador e segurança de senha em ServicoCadastroUsuario
 * Tipo   : Unitário
 * Fase   : Verificação
 * Resp.  : Igor Farias
 */
@ExtendWith(MockitoExtension.class)
class ServicoCadastroUsuarioTest {

    @Mock private RepositorioUsuario repositorioUsuario;
    @Mock private PasswordEncoder    passwordEncoder;

    @InjectMocks
    private ServicoCadastroUsuario servicoCadastroUsuario;

    // ------------------------------------------------------------------ //
    // Cenário A — cadastro com sucesso                                    //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioA_cadastrarAdministrador_deveSalvarUsuarioComSenhaHasheadaEPerfilAdministrador() {
        // Arrange
        CadastroUsuarioRequest request = new CadastroUsuarioRequest();
        request.setNome("Admin Geral");
        request.setEmail("admin.geral@koch.com");
        request.setSenha("senhaSegura123");

        when(repositorioUsuario.existePorEmail("admin.geral@koch.com")).thenReturn(false);
        when(passwordEncoder.encode("senhaSegura123")).thenReturn("$2a$hash_simulado");
        when(repositorioUsuario.salvar(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        servicoCadastroUsuario.cadastrarAdministrador(request);

        // Assert — senha hasheada, perfil ADMINISTRADOR e UUID gerado
        verify(passwordEncoder, times(1)).encode("senhaSegura123");
        verify(repositorioUsuario, times(1)).salvar(argThat(u ->
                "Admin Geral".equals(u.getNome())                    &&
                        "admin.geral@koch.com".equals(u.getEmail())          &&
                        "$2a$hash_simulado".equals(u.getSenhaHash())          &&
                        Perfil.ADMINISTRADOR == u.getPerfil()                  &&
                        u.getId() != null
        ));
    }

    // ------------------------------------------------------------------ //
    // Cenário B — conflito de e-mail                                      //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioB_cadastrarAdministrador_deveLancarExcecaoQuandoEmailJaCadastrado() {
        // Arrange
        CadastroUsuarioRequest request = new CadastroUsuarioRequest();
        request.setNome("Admin Reserva");
        request.setEmail("admin.geral@koch.com");
        request.setSenha("outraSenha123");

        when(repositorioUsuario.existePorEmail("admin.geral@koch.com")).thenReturn(true);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> servicoCadastroUsuario.cadastrarAdministrador(request)
        );
        assertEquals("Já existe um usuário com esse e-mail.", ex.getMessage());
        verify(passwordEncoder,    never()).encode(any());
        verify(repositorioUsuario, never()).salvar(any());
    }
}