package br.com.koch.modelo.cliente;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * CT-02 — Validar integridade e encapsulamento da entidade Cliente
 * Tipo   : Unitário
 * Fase   : Verificação
 * Resp.  : Igor Farias
 */
class ClienteTest {

    @Test
    void deveArmazenarERecuperarAtributosComRelacionamentoUsuario() {
        // Arrange
        UsuarioCliente usuarioTeste = new UsuarioCliente();
        usuarioTeste.setId(1);
        usuarioTeste.setTipo("CLIENTE");
        usuarioTeste.setEmail("cliente@email.com");
        usuarioTeste.setSenha("senhaDeTeste123");

        Cliente cliente = new Cliente();

        // Act
        cliente.setId(1);
        cliente.setNome("Igor Farias");
        cliente.setTelefone("4599999999");
        cliente.setEndereco("Rua Alberto Nepomuceno, 123");
        cliente.setUsuario(usuarioTeste);

        // Assert
        assertEquals(1, cliente.getId());
        assertEquals("Igor Farias", cliente.getNome());
        assertEquals("4599999999", cliente.getTelefone());
        assertEquals("Rua Alberto Nepomuceno, 123", cliente.getEndereco());
        assertSame(usuarioTeste, cliente.getUsuario());
        assertEquals(1, cliente.getUsuario().getId());
    }
}