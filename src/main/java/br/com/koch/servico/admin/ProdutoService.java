package br.com.koch.servico.admin;

import br.com.koch.modelo.admin.Produto;
import br.com.koch.repositorio.admin.PedidoRepository;
import br.com.koch.repositorio.admin.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;
    private final ServicoArquivoImagem servicoArquivoImagem;

    public ProdutoService(
            ProdutoRepository produtoRepository,
            PedidoRepository pedidoRepository,
            ServicoArquivoImagem servicoArquivoImagem
    ) {
        this.produtoRepository = produtoRepository;
        this.pedidoRepository = pedidoRepository;
        this.servicoArquivoImagem = servicoArquivoImagem;
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id).orElse(null);
    }

    public void salvar(Produto produto, MultipartFile imagemArquivo) throws IOException {
        if (produto.getNome() == null || produto.getNome().isBlank()) {
            throw new IllegalArgumentException("Informe o nome da cesta.");
        }
        if (produto.getPreco() == null) {
            throw new IllegalArgumentException("Informe o preço da cesta.");
        }

        if (produto.getId() != null) {
            Produto existente = buscarPorId(produto.getId());
            if (existente != null && (imagemArquivo == null || imagemArquivo.isEmpty())) {
                produto.setImagemUrl(existente.getImagemUrl());
            }
        }

        if (imagemArquivo != null && !imagemArquivo.isEmpty()) {
            produto.setImagemUrl(servicoArquivoImagem.salvarImagemCesta(imagemArquivo));
        }

        produtoRepository.save(produto);
    }

    @Transactional
    public void deletar(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new IllegalArgumentException("Cesta não encontrada.");
        }
        pedidoRepository.deleteByProduto_Id(id);
        produtoRepository.deleteById(id);
    }
}
