package br.com.koch.repositorio.admin;

import br.com.koch.modelo.admin.Pedido;
import br.com.koch.modelo.admin.StatusPedido;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;

public class PedidoRepositoryImpl implements PedidoRepository {

    private List<Pedido> pedidos = new ArrayList<>();
    private Long proximoId = 1L;

    @Override
    public List<Pedido> listarTodos() {
        return pedidos;
    }

    @Override
    public Pedido buscarPorId(Long id) {
        return pedidos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void salvar(Pedido pedido) {
        if (pedido.getId() == null) {
            pedido.setId(proximoId++);
            pedidos.add(pedido);
        } else {
            deletar(pedido.getId());
            pedidos.add(pedido);
        }
    }

    @Override
    public void deletar(Long id) {
        pedidos.removeIf(p -> p.getId().equals(id));
    }

    @Override
    public List<Pedido> buscarPorStatus(StatusPedido status) {
        return pedidos.stream()
                .filter(p -> p.getStatus().equals(status))
                .collect(Collectors.toList());
    }
}
