package br.com.koch.modelo.admin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * CT-05 — Validar integridade e encapsulamento da entidade ClienteListar
 * Tipo   : Unitário
 * Fase   : Verificação
 * Resp.  : Igor Farias
 */
class ClienteListarTest {

    @Test
    void deveArmazenarERecuperarAtributosCorretamente() {
        // Arrange
        ClienteListar clienteListar = new ClienteListar();

        // Act
        clienteListar.setId(1L);
        clienteListar.setNome("Ana Silva");
        clienteListar.setTelefone("45988888888");
        clienteListar.setEndereco("Avenida Brasil, 456");

        // Assert
        assertEquals(1L, clienteListar.getId());
        assertEquals("Ana Silva", clienteListar.getNome());
        assertEquals("45988888888", clienteListar.getTelefone());
        assertEquals("Avenida Brasil, 456", clienteListar.getEndereco());
    }
}