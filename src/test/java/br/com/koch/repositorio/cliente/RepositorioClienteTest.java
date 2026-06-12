package br.com.koch.repositorio.cliente;

import br.com.koch.modelo.cliente.Cliente;
import br.com.koch.modelo.cliente.UsuarioCliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

/**
 * CT-26 — Validar busca por e-mail de usuário vinculado em RepositorioCliente
 * Tipo   : Integração (Persistência)
 * Fase   : Verificação
 * Resp.  : Igor Farias
 */
@DataJpaTest
class RepositorioClienteTest {

    @Autowired private RepositorioCliente        repositorioCliente;
    @Autowired private RepositorioUsuarioCliente  repositorioUsuarioCliente;

    @BeforeEach
    void configurar() {
        // UsuarioCliente precisa ser persistido antes do Cliente (FK)
        UsuarioCliente usuario = new UsuarioCliente();
        usuario.setTipo("CLIENTE");
        usuario.setEmail("cliente@koch.com");
        usuario.setSenha("$2a$hash");
        repositorioUsuarioCliente.save(usuario);

        Cliente cliente = new Cliente();
        cliente.setNome("Ana Silva");
        cliente.setTelefone("45988888888");
        cliente.setEndereco("Rua Alberto Nepomuceno, 123");
        cliente.setUsuario(usuario);
        repositorioCliente.save(cliente);
    }

    // ------------------------------------------------------------------ //
    // Cenário A — busca por e-mail com sucesso                            //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioA_findByUsuarioEmail_deveRetornarClienteQuandoEmailExiste() {
        // Act
        Optional<Cliente> resultado = repositorioCliente.findByUsuario_Email("cliente@koch.com");

        // Assert
        assertTrue(resultado.isPresent(),
                "Deve retornar o cliente quando o e-mail existe");

        Cliente encontrado = resultado.get();
        assertEquals("Ana Silva", encontrado.getNome());
        assertEquals("45988888888", encontrado.getTelefone());
        assertEquals("Rua Alberto Nepomuceno, 123", encontrado.getEndereco());
        assertEquals("cliente@koch.com", encontrado.getUsuario().getEmail());
    }

    // ------------------------------------------------------------------ //
    // Cenário B — e-mail inexistente                                      //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioB_findByUsuarioEmail_deveRetornarEmptyQuandoEmailNaoExiste() {
        // Act
        Optional<Cliente> resultado = repositorioCliente.findByUsuario_Email("nao_existe@koch.com");

        // Assert
        assertTrue(resultado.isEmpty(),
                "Deve retornar Optional.empty() para e-mail não cadastrado");
    }
}