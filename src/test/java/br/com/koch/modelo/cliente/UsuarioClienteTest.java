package br.com.koch.modelo.cliente;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * CT-01 — Validar integridade e encapsulamento da entidade UsuarioCliente
 * Tipo   : Unitário
 * Fase   : Verificação
 * Resp.  : Igor Farias
 */
public class UsuarioClienteTest {
    @Test
    void deveArmazenarERecuperarAtributosCorretamente() {
        // Arrange
        UsuarioCliente usuario = new UsuarioCliente();

        // Act
        usuario.setId(1);
        usuario.setTipo("CLIENTE");
        usuario.setEmail("cliente@email.com");
        usuario.setSenha("senhaDeTeste123");

        // Assert
        assertEquals(1, usuario.getId());
        assertEquals("CLIENTE", usuario.getTipo());
        assertEquals("cliente@email.com", usuario.getEmail());
        assertEquals("senhaDeTeste123", usuario.getSenha());
    }

}
