package br.com.koch.repositorio.admin;

import br.com.koch.modelo.admin.Produto;
import java.util.List;

public interface ProdutoRepository {

    List<Produto> listarTodos();
    Produto buscarPorId(Long id);
    void salvar(Produto produto);
    void deletar(Long id);
}
