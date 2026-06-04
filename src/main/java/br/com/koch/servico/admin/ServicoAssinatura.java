package br.com.koch.servico.admin;

import br.com.koch.modelo.admin.Pedido;
import br.com.koch.modelo.admin.Produto;
import br.com.koch.modelo.cliente.Cliente;
import br.com.koch.repositorio.admin.PedidoRepository;
import br.com.koch.repositorio.admin.ProdutoRepository;
import br.com.koch.repositorio.cliente.RepositorioCliente;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServicoAssinatura {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final RepositorioCliente repositorioCliente;

    public ServicoAssinatura(
            PedidoRepository pedidoRepository,
            ProdutoRepository produtoRepository,
            RepositorioCliente repositorioCliente
    ) {
        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.repositorioCliente = repositorioCliente;
    }

    @Transactional
    public Long assinarCesta(Long produtoId, String emailCliente) {
        Cliente cliente = repositorioCliente.findByUsuario_Email(emailCliente)
                .orElseThrow(() -> new IllegalStateException("Cliente não encontrado."));

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Cesta não encontrada."));

        if (pedidoRepository.existsByCliente_IdAndProduto_IdAndAtivoTrue(cliente.getId(), produtoId)) {
            throw new IllegalArgumentException("Você já possui esta assinatura ativa.");
        }

        Pedido pedido = new Pedido();
        pedido.setNomeAssinatura(produto.getNome());
        pedido.setPreco(produto.getPreco());
        pedido.setAtivo(true);
        pedido.setProduto(produto);
        pedido.setCliente(cliente);
        return pedidoRepository.save(pedido).getId();
    }

    public Optional<Long> buscarIdAssinaturaAtivaPorProduto(String emailCliente, Long produtoId) {
        return repositorioCliente.findByUsuario_Email(emailCliente)
                .flatMap(cliente -> pedidoRepository
                        .findFirstByCliente_IdAndProduto_IdAndAtivoTrue(cliente.getId(), produtoId)
                        .map(Pedido::getId));
    }

    public Map<Long, Long> mapaAssinaturasAtivasPorProduto(Integer clienteId) {
        return listarPorCliente(clienteId).stream()
                .filter(p -> Boolean.TRUE.equals(p.getAtivo()) && p.getProduto() != null)
                .collect(Collectors.toMap(
                        p -> p.getProduto().getId(),
                        Pedido::getId,
                        (existente, duplicado) -> existente
                ));
    }

    public List<Pedido> listarPorCliente(Integer clienteId) {
        return pedidoRepository.findByCliente_Id(clienteId);
    }

    @Transactional
    public void desativarAssinatura(Long assinaturaId, String emailCliente) {
        Cliente cliente = repositorioCliente.findByUsuario_Email(emailCliente)
                .orElseThrow(() -> new IllegalStateException("Cliente não encontrado."));

        Pedido pedido = pedidoRepository.findById(assinaturaId)
                .orElseThrow(() -> new IllegalArgumentException("Assinatura não encontrada."));

        if (!pedido.getCliente().getId().equals(cliente.getId())) {
            throw new IllegalArgumentException("Você não pode alterar esta assinatura.");
        }

        if (!Boolean.TRUE.equals(pedido.getAtivo())) {
            throw new IllegalArgumentException("Esta assinatura já está desativada.");
        }

        pedido.setAtivo(false);
        pedidoRepository.save(pedido);
    }
}
