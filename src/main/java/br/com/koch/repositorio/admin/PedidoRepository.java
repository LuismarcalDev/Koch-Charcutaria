package br.com.koch.repositorio.admin;

import java.util.List;
import br.com.koch.modelo.admin.Pedido;
import br.com.koch.modelo.admin.StatusPedido;

public interface PedidoRepository {

    List<Pedido> listarTodos();
    Pedido buscarPorId(Long id);
    void salvar(Pedido pedido);
    void deletar(Long id);
    List<Pedido> buscarPorStatus(StatusPedido status);
}
