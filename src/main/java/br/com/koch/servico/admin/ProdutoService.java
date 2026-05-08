package br.com.koch.servico.admin;

import br.com.koch.modelo.admin.Produto;
import br.com.koch.repositorio.admin.ProdutoRepository;
import br.com.koch.repositorio.admin.ProdutoRepositoryImpl;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProdutoService {

    private ProdutoRepository produtoRepository = new ProdutoRepositoryImpl();

    public List<Produto> listarTodos() {
        return produtoRepository.listarTodos();
    }

    public Produto buscarPorId(Long id) {
        return produtoRepository.buscarPorId(id);
    }

    public void salvar(Produto produto) {
        produtoRepository.salvar(produto);
    }

    public void deletar(Long id) {
        produtoRepository.deletar(id);
    }
}
