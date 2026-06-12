package br.com.koch.modelo.admin;

import br.com.koch.modelo.cliente.Cliente;
import br.com.koch.modelo.cliente.UsuarioCliente;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

/**
 * CT-04 — Validar integridade e encapsulamento da entidade Pedido
 * Tipo   : Unitário
 * Fase   : Verificação
 * Resp.  : Igor Farias
 */
class PedidoTest {

    @Test
    void deveArmazenarERecuperarAtributosComRelacionamentosManyToOne() {
        // Arrange — ProdutoTeste
        Produto produtoTeste = new Produto();
        produtoTeste.setId(1L);
        produtoTeste.setNome("Copa Defumada");
        produtoTeste.setPreco(new BigDecimal("45.90"));

        // Arrange — ClienteTeste (exige UsuarioCliente por @OneToOne optional=false)
        UsuarioCliente usuarioTeste = new UsuarioCliente();
        usuarioTeste.setId(1);
        usuarioTeste.setEmail("cliente@email.com");

        Cliente clienteTeste = new Cliente();
        clienteTeste.setId(1);
        clienteTeste.setNome("Igor Farias");
        clienteTeste.setUsuario(usuarioTeste);

        Pedido pedido = new Pedido();
        BigDecimal precoEsperado = new BigDecimal("79.90");

        // Act
        pedido.setId(1L);
        pedido.setNomeAssinatura("Plano Mensal Premium");
        pedido.setPreco(precoEsperado);
        pedido.setAtivo(true);
        pedido.setProduto(produtoTeste);
        pedido.setCliente(clienteTeste);

        // Assert — atributos primitivos
        assertEquals(1L, pedido.getId());
        assertEquals("Plano Mensal Premium", pedido.getNomeAssinatura());
        assertEquals(0, precoEsperado.compareTo(pedido.getPreco()));
        assertTrue(pedido.getAtivo());

        // Assert — relacionamentos ManyToOne
        assertSame(produtoTeste, pedido.getProduto());
        assertEquals("Copa Defumada", pedido.getProduto().getNome());

        assertSame(clienteTeste, pedido.getCliente());
        assertEquals("Igor Farias", pedido.getCliente().getNome());
    }
}