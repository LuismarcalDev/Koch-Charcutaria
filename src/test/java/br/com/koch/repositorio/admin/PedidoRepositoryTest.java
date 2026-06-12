package br.com.koch.repositorio.admin;

import br.com.koch.modelo.admin.Pedido;
import br.com.koch.modelo.admin.Produto;
import br.com.koch.modelo.cliente.Cliente;
import br.com.koch.modelo.cliente.UsuarioCliente;
import br.com.koch.repositorio.admin.PedidoRepository;
import br.com.koch.repositorio.admin.ProdutoRepository;
import br.com.koch.repositorio.cliente.RepositorioCliente;
import br.com.koch.repositorio.cliente.RepositorioUsuarioCliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * CT-24 — Validar consultas derivadas (Query Methods) em PedidoRepository
 * Tipo   : Integração (Persistência)
 * Fase   : Verificação
 * Resp.  : Igor Farias
 */
@DataJpaTest
class PedidoRepositoryTest {

    @Autowired private PedidoRepository          pedidoRepository;
    @Autowired private ProdutoRepository          produtoRepository;
    @Autowired private RepositorioCliente         repositorioCliente;
    @Autowired private RepositorioUsuarioCliente  repositorioUsuarioCliente;

    private Cliente  cliente1;
    private Cliente  cliente2;
    private Produto  produto;

    @BeforeEach
    void configurar() {
        // UsuarioCliente 1
        UsuarioCliente usuario1 = new UsuarioCliente();
        usuario1.setTipo("CLIENTE");
        usuario1.setEmail("cliente1@email.com");
        usuario1.setSenha("$2a$hash1");
        repositorioUsuarioCliente.save(usuario1);

        // Cliente 1
        cliente1 = new Cliente();
        cliente1.setNome("Ana Silva");
        cliente1.setTelefone("45988888888");
        cliente1.setEndereco("Rua A, 1");
        cliente1.setUsuario(usuario1);
        repositorioCliente.save(cliente1);

        // UsuarioCliente 2
        UsuarioCliente usuario2 = new UsuarioCliente();
        usuario2.setTipo("CLIENTE");
        usuario2.setEmail("cliente2@email.com");
        usuario2.setSenha("$2a$hash2");
        repositorioUsuarioCliente.save(usuario2);

        // Cliente 2
        cliente2 = new Cliente();
        cliente2.setNome("Igor Farias");
        cliente2.setTelefone("45977777777");
        cliente2.setEndereco("Rua B, 2");
        cliente2.setUsuario(usuario2);
        repositorioCliente.save(cliente2);

        // Produto
        produto = new Produto();
        produto.setNome("Cesta Colonial");
        produto.setPreco(new BigDecimal("120.00"));
        produtoRepository.save(produto);
    }

    // ── helper ───────────────────────────────────────────────────────── //

    private Pedido criarPedido(Cliente cliente, Produto prod, boolean ativo) {
        Pedido p = new Pedido();
        p.setNomeAssinatura(prod.getNome());
        p.setPreco(prod.getPreco());
        p.setAtivo(ativo);
        p.setProduto(prod);
        p.setCliente(cliente);
        return pedidoRepository.save(p);
    }

    // ------------------------------------------------------------------ //
    // Cenário A — existsByCliente_IdAndProduto_IdAndAtivoTrue             //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioA_existsByClienteIdAndProdutoIdAndAtivoTrue_deveRetornarTrueParaAssinaturaAtiva() {
        // Arrange
        criarPedido(cliente1, produto, true);

        // Act + Assert
        boolean existe = pedidoRepository
                .existsByCliente_IdAndProduto_IdAndAtivoTrue(cliente1.getId(), produto.getId());
        assertTrue(existe,
                "Deve retornar true para assinatura ativa do cliente com o produto");
    }

    @Test
    void cenarioA_existsByClienteIdAndProdutoIdAndAtivoTrue_deveRetornarFalseParaAssinaturaInativa() {
        // Arrange
        criarPedido(cliente1, produto, false);

        // Act + Assert
        boolean existe = pedidoRepository
                .existsByCliente_IdAndProduto_IdAndAtivoTrue(cliente1.getId(), produto.getId());
        assertFalse(existe,
                "Deve retornar false para assinatura inativa");
    }

    // ------------------------------------------------------------------ //
    // Cenário B — findByCliente_Id                                        //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioB_findByClienteId_deveRetornarApenasOsPedidosDoClienteCorreto() {
        // Arrange — 2 pedidos para cliente1, 1 para cliente2
        Pedido p1 = criarPedido(cliente1, produto, true);
        Pedido p2 = criarPedido(cliente1, produto, false);
        criarPedido(cliente2, produto, true);

        // Act
        List<Pedido> resultado = pedidoRepository.findByCliente_Id(cliente1.getId());

        // Assert
        assertEquals(2, resultado.size(),
                "Deve retornar exatamente 2 pedidos para o cliente1");
        assertTrue(resultado.stream().allMatch(p ->
                        p.getCliente().getId().equals(cliente1.getId())),
                "Todos os pedidos retornados devem pertencer ao cliente1");
        assertTrue(resultado.stream().map(Pedido::getId).toList()
                .containsAll(List.of(p1.getId(), p2.getId())));
    }

    // ------------------------------------------------------------------ //
    // Cenário C — deleteByProduto_Id                                      //
    // ------------------------------------------------------------------ //

    @Test
    @Transactional
    void cenarioC_deleteByProdutoId_deveRemoverTodosOsPedidosVinculadosAoProduto() {
        // Arrange — 2 pedidos vinculados ao produto
        criarPedido(cliente1, produto, true);
        criarPedido(cliente2, produto, false);

        // Act
        pedidoRepository.deleteByProduto_Id(produto.getId());
        pedidoRepository.flush();

        // Assert
        List<Pedido> restantes = pedidoRepository.findAll();
        assertTrue(restantes.isEmpty(),
                "Todos os pedidos vinculados ao produto devem ser removidos");
    }
}