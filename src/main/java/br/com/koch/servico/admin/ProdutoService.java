package br.com.koch.servico.admin;

import br.com.koch.modelo.admin.Produto;
import br.com.koch.repositorio.admin.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ServicoArquivoImagem servicoArquivoImagem;

    public ProdutoService(ProdutoRepository produtoRepository, ServicoArquivoImagem servicoArquivoImagem) {
        this.produtoRepository = produtoRepository;
        this.servicoArquivoImagem = servicoArquivoImagem;
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.findById(id).orElse(null);
    }

    public void salvar(Produto produto, MultipartFile imagemArquivo) throws IOException {
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

    public void deletar(Long id) {
        produtoRepository.deleteById(id);
    }
}
