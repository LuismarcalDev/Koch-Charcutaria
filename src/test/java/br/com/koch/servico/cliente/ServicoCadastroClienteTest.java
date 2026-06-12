package br.com.koch.servico.cliente;

import br.com.koch.dto.cliente.CadastroClienteRequest;
import br.com.koch.modelo.cliente.Cliente;
import br.com.koch.modelo.cliente.UsuarioCliente;
import br.com.koch.repositorio.cliente.RepositorioCliente;
import br.com.koch.repositorio.cliente.RepositorioUsuarioCliente;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * CT-18 — Validar cadastro de cliente, criptografia de senha e unicidade de e-mail em ServicoCadastroCliente
 * Tipo   : Unitário
 * Fase   : Verificação
 * Resp.  : Igor Farias
 */
@ExtendWith(MockitoExtension.class)
class ServicoCadastroClienteTest {

    @Mock private RepositorioUsuarioCliente repositorioUsuarioCliente;
    @Mock private RepositorioCliente        repositorioCliente;
    @Mock private PasswordEncoder           passwordEncoder;

    @InjectMocks
    private ServicoCadastroCliente servicoCadastroCliente;

    // ------------------------------------------------------------------ //
    // Cenário A — cadastro com sucesso                                    //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioA_cadastrar_deveSalvarUsuarioClienteEClienteEmSequencia() {
        // Arrange
        CadastroClienteRequest request = new CadastroClienteRequest();
        request.setNome("Ana Silva");
        request.setEmail("ana@email.com");
        request.setTelefone("45988888888");
        request.setEndereco("Avenida Brasil, 456");
        request.setSenha("senhaCliente123");

        UsuarioCliente usuarioSalvo = new UsuarioCliente();
        usuarioSalvo.setId(1);
        usuarioSalvo.setEmail("ana@email.com");

        when(repositorioUsuarioCliente.existsByEmail("ana@email.com")).thenReturn(false);
        when(passwordEncoder.encode("senhaCliente123")).thenReturn("$2a$hash_simulado");
        when(repositorioUsuarioCliente.save(any(UsuarioCliente.class))).thenReturn(usuarioSalvo);

        // Act
        servicoCadastroCliente.cadastrar(request);

        // Assert — UsuarioCliente salvo com tipo, email e senha hasheada
        verify(passwordEncoder, times(1)).encode("senhaCliente123");
        verify(repositorioUsuarioCliente, times(1)).save(argThat(u ->
                "CLIENTE".equals(u.getTipo())             &&
                        "ana@email.com".equals(u.getEmail())      &&
                        "$2a$hash_simulado".equals(u.getSenha())
        ));

        // Assert — Cliente salvo com dados do request e referência ao usuário salvo
        verify(repositorioCliente, times(1)).save(argThat(c ->
                "Ana Silva".equals(c.getNome())              &&
                        "45988888888".equals(c.getTelefone())         &&
                        "Avenida Brasil, 456".equals(c.getEndereco()) &&
                        usuarioSalvo.equals(c.getUsuario())
        ));
    }

    // ------------------------------------------------------------------ //
    // Cenário B — e-mail duplicado                                        //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioB_cadastrar_deveLancarExcecaoQuandoEmailJaCadastrado() {
        // Arrange
        CadastroClienteRequest request = new CadastroClienteRequest();
        request.setNome("Ana Souza");
        request.setEmail("ana@email.com");
        request.setTelefone("45977777777");
        request.setEndereco("Rua XV de Novembro, 789");
        request.setSenha("outraSenha123");

        when(repositorioUsuarioCliente.existsByEmail("ana@email.com")).thenReturn(true);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> servicoCadastroCliente.cadastrar(request)
        );
        assertEquals("Já existe um cliente com esse e-mail.", ex.getMessage());
        verify(passwordEncoder,           never()).encode(any());
        verify(repositorioUsuarioCliente, never()).save(any());
        verify(repositorioCliente,         never()).save(any());
    }
}