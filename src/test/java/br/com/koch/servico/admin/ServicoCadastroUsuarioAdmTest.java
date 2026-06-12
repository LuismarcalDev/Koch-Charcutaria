package br.com.koch.servico.admin;

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
 * CT-16 — Validar cadastro de administrador e unicidade de e-mail em ServicoCadastroUsuarioAdm
 * Tipo   : Unitário
 * Fase   : Verificação
 * Resp.  : Igor Farias
 */
@ExtendWith(MockitoExtension.class)
class ServicoCadastroUsuarioAdmTest {

    @Mock private RepositorioUsuario repositorioUsuario;
    @Mock private PasswordEncoder    passwordEncoder;

    @InjectMocks
    private ServicoCadastroUsuarioAdm servicoCadastroUsuarioAdm;

    // ------------------------------------------------------------------ //
    // Cenário A — cadastro com sucesso                                    //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioA_cadastrarAdministrador_deveSalvarUsuarioComSenhaHasheadaEPerfilAdministrador() {
        // Arrange
        CadastroUsuarioRequest request = new CadastroUsuarioRequest();
        request.setNome("Igor Adm");
        request.setEmail("adm@koch.com");
        request.setSenha("senhaSegura");

        when(repositorioUsuario.existePorEmail("adm@koch.com")).thenReturn(false);
        when(passwordEncoder.encode("senhaSegura")).thenReturn("$2a$hash_simulado");
        when(repositorioUsuario.salvar(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        servicoCadastroUsuarioAdm.cadastrarAdministrador(request);

        // Assert — senha nunca salva em texto limpo; perfil obrigatoriamente ADMINISTRADOR
        verify(passwordEncoder, times(1)).encode("senhaSegura");
        verify(repositorioUsuario, times(1)).salvar(argThat(u ->
                "Igor Adm".equals(u.getNome())                  &&
                        "adm@koch.com".equals(u.getEmail())             &&
                        "$2a$hash_simulado".equals(u.getSenhaHash())    &&
                        Perfil.ADMINISTRADOR == u.getPerfil()             &&
                        u.getId() != null
        ));
    }

    // ------------------------------------------------------------------ //
    // Cenário B — e-mail já cadastrado                                    //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioB_cadastrarAdministrador_deveLancarExcecaoQuandoEmailJaCadastrado() {
        // Arrange
        CadastroUsuarioRequest request = new CadastroUsuarioRequest();
        request.setNome("Outro Adm");
        request.setEmail("adm@koch.com");
        request.setSenha("outraSenha");

        when(repositorioUsuario.existePorEmail("adm@koch.com")).thenReturn(true);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> servicoCadastroUsuarioAdm.cadastrarAdministrador(request)
        );
        assertEquals("Já existe um usuário com esse e-mail.", ex.getMessage());
        verify(passwordEncoder, never()).encode(any());
        verify(repositorioUsuario, never()).salvar(any());
    }
}