package br.com.koch.repositorio.cliente;

import br.com.koch.modelo.cliente.UsuarioCliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

/**
 * CT-27 — Validar busca e verificação de existência por e-mail em RepositorioUsuarioCliente
 * Tipo   : Integração (Persistência)
 * Fase   : Verificação
 * Resp.  : Igor Farias
 */
@DataJpaTest
class RepositorioUsuarioClienteTest {

    @Autowired
    private RepositorioUsuarioCliente repositorioUsuarioCliente;

    @BeforeEach
    void configurar() {
        UsuarioCliente usuario = new UsuarioCliente();
        usuario.setTipo("CLIENTE");
        usuario.setEmail("usuario.cliente@koch.com");
        usuario.setSenha("$2a$10$hashSimulado");
        repositorioUsuarioCliente.save(usuario);
    }

    // ------------------------------------------------------------------ //
    // Cenário A — findByEmail com sucesso                                 //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioA_findByEmail_deveRetornarUsuarioQuandoEmailExiste() {
        // Act
        Optional<UsuarioCliente> resultado =
                repositorioUsuarioCliente.findByEmail("usuario.cliente@koch.com");

        // Assert
        assertTrue(resultado.isPresent(),
                "Deve retornar o usuário quando o e-mail existe");

        UsuarioCliente encontrado = resultado.get();
        assertEquals("usuario.cliente@koch.com", encontrado.getEmail());
        assertEquals("CLIENTE", encontrado.getTipo());
        assertEquals("$2a$10$hashSimulado", encontrado.getSenha());
        assertNotNull(encontrado.getId(),
                "O ID deve ter sido gerado pelo banco");
    }

    // ------------------------------------------------------------------ //
    // Cenário B — existsByEmail para e-mail cadastrado                    //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioB_existsByEmail_deveRetornarTrueQuandoEmailExiste() {
        // Act + Assert
        assertTrue(
                repositorioUsuarioCliente.existsByEmail("usuario.cliente@koch.com"),
                "Deve retornar true para e-mail cadastrado"
        );
    }

    // ------------------------------------------------------------------ //
    // Cenário C — e-mail inexistente (findByEmail e existsByEmail)        //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioC_findByEmail_deveRetornarEmptyQuandoEmailNaoExiste() {
        // Act
        Optional<UsuarioCliente> resultado =
                repositorioUsuarioCliente.findByEmail("inexistente@koch.com");

        // Assert
        assertTrue(resultado.isEmpty(),
                "Deve retornar Optional.empty() para e-mail não cadastrado");
    }

    @Test
    void cenarioC_existsByEmail_deveRetornarFalseQuandoEmailNaoExiste() {
        // Act + Assert
        assertFalse(
                repositorioUsuarioCliente.existsByEmail("inexistente@koch.com"),
                "Deve retornar false para e-mail não cadastrado"
        );
    }
}