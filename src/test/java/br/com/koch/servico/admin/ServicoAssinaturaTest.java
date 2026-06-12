package br.com.koch.servico.admin;

import br.com.koch.modelo.admin.Pedido;
import br.com.koch.modelo.admin.Produto;
import br.com.koch.modelo.cliente.Cliente;
import br.com.koch.modelo.cliente.UsuarioCliente;
import br.com.koch.repositorio.admin.PedidoRepository;
import br.com.koch.repositorio.admin.ProdutoRepository;
import br.com.koch.repositorio.cliente.RepositorioCliente;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * CT-15 — Validar ciclo de vida de assinaturas, segurança de posse e exceções em ServicoAssinatura
 * Tipo   : Unitário
 * Fase   : Verificação
 * Resp.  : Igor Farias
 */
@ExtendWith(MockitoExtension.class)
class ServicoAssinaturaTest {

    @Mock private PedidoRepository    pedidoRepository;
    @Mock private ProdutoRepository   produtoRepository;
    @Mock private RepositorioCliente  repositorioCliente;

    @InjectMocks
    private ServicoAssinatura servicoAssinatura;

    // ── helpers ──────────────────────────────────────────────────────── //

    private Cliente criarCliente(int id, String email) {
        UsuarioCliente usuario = new UsuarioCliente();
        usuario.setEmail(email);

        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setUsuario(usuario);
        return cliente;
    }

    private Produto criarProduto(long id, String nome) {
        Produto produto = new Produto();
        produto.setId(id);
        produto.setNome(nome);
        produto.setPreco(new BigDecimal("120.00"));
        return produto;
    }

    private Pedido criarPedido(long id, Cliente cliente, boolean ativo) {
        Pedido pedido = new Pedido();
        pedido.setId(id);
        pedido.setCliente(cliente);
        pedido.setAtivo(ativo);
        return pedido;
    }

    // ------------------------------------------------------------------ //
    // Cenário A — assinar cesta com sucesso                               //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioA_assinarCesta_devePersistirPedidoAtivoERetornarId() {
        // Arrange
        Cliente cliente   = criarCliente(5, "cliente@email.com");
        Produto produto   = criarProduto(1L, "Cesta Colonial");

        Pedido pedidoSalvo = criarPedido(10L, cliente, true);

        when(repositorioCliente.findByUsuario_Email("cliente@email.com"))
                .thenReturn(Optional.of(cliente));
        when(produtoRepository.findById(1L))
                .thenReturn(Optional.of(produto));
        when(pedidoRepository.existsByCliente_IdAndProduto_IdAndAtivoTrue(5, 1L))
                .thenReturn(false);
        when(pedidoRepository.save(any(Pedido.class)))
                .thenReturn(pedidoSalvo);

        // Act
        Long idRetornado = servicoAssinatura.assinarCesta(1L, "cliente@email.com");

        // Assert
        assertEquals(10L, idRetornado,
                "O ID do pedido salvo deve ser retornado");
        verify(pedidoRepository, times(1)).save(argThat(p ->
                Boolean.TRUE.equals(p.getAtivo()) &&
                        p.getCliente().getId().equals(5) &&
                        p.getProduto().getId().equals(1L)
        ));
    }

    // ------------------------------------------------------------------ //
    // Cenário B — bloqueio de assinatura duplicada                        //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioB_assinarCesta_deveLancarExcecaoQuandoAssinaturaJaAtiva() {
        // Arrange
        Cliente cliente = criarCliente(5, "cliente@email.com");
        Produto produto  = criarProduto(1L, "Cesta Colonial");

        when(repositorioCliente.findByUsuario_Email("cliente@email.com"))
                .thenReturn(Optional.of(cliente));
        when(produtoRepository.findById(1L))
                .thenReturn(Optional.of(produto));
        when(pedidoRepository.existsByCliente_IdAndProduto_IdAndAtivoTrue(5, 1L))
                .thenReturn(true);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> servicoAssinatura.assinarCesta(1L, "cliente@email.com")
        );
        assertEquals("Você já possui esta assinatura ativa.", ex.getMessage());
        verify(pedidoRepository, never()).save(any());
    }

    // ------------------------------------------------------------------ //
    // Cenário C — desativar assinatura com sucesso                        //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioC_desativarAssinatura_deveAlterarFlagAtivoParaFalse() {
        // Arrange
        Cliente cliente = criarCliente(5, "cliente@email.com");
        Pedido  pedido  = criarPedido(10L, cliente, true);

        when(repositorioCliente.findByUsuario_Email("cliente@email.com"))
                .thenReturn(Optional.of(cliente));
        when(pedidoRepository.findById(10L))
                .thenReturn(Optional.of(pedido));

        // Act
        servicoAssinatura.desativarAssinatura(10L, "cliente@email.com");

        // Assert
        assertFalse(pedido.getAtivo(),
                "O flag ativo deve ser false após desativar a assinatura");
        verify(pedidoRepository, times(1)).save(pedido);
    }

    // ------------------------------------------------------------------ //
    // Cenário D — violação de segurança no cancelamento                   //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioD_desativarAssinatura_deveLancarExcecaoQuandoClienteNaoPossuiAssinatura() {
        // Arrange — pedido pertence ao cliente 9, mas é acionado pelo cliente 5
        Cliente clienteDono      = criarCliente(9, "outro@email.com");
        Cliente clienteInvasor   = criarCliente(5, "cliente@email.com");
        Pedido  pedido            = criarPedido(10L, clienteDono, true);

        when(repositorioCliente.findByUsuario_Email("cliente@email.com"))
                .thenReturn(Optional.of(clienteInvasor));
        when(pedidoRepository.findById(10L))
                .thenReturn(Optional.of(pedido));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> servicoAssinatura.desativarAssinatura(10L, "cliente@email.com")
        );
        assertEquals("Você não pode alterar esta assinatura.", ex.getMessage());
        verify(pedidoRepository, never()).save(any());
    }
}