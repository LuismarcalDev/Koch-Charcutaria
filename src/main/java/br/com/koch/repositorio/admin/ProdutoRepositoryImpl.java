package br.com.koch.repositorio.admin;

import br.com.koch.modelo.admin.Produto;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepositoryImpl implements ProdutoRepository {

    private List<Produto> produtos = new ArrayList<>();
    private Long proximoId = 1L;

    @Override
    public List<Produto> listarTodos() {
        return produtos;
    }

    @Override
    public Produto buscarPorId(Long id) {
        return produtos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void salvar(Produto produto) {
        if (produto.getId() == null) {
            produto.setId(proximoId++);
            produtos.add(produto);
        } else {
            deletar(produto.getId());
            produtos.add(produto);
        }
    }

    @Override
    public void deletar(Long id) {
        produtos.removeIf(p -> p.getId().equals(id));
    }
}
