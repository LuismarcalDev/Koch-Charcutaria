package br.com.koch.servico.admin;

import br.com.koch.modelo.admin.Pedido;
import br.com.koch.modelo.admin.StatusPedido;
import br.com.koch.repositorio.admin.PedidoRepository;
import br.com.koch.repositorio.admin.PedidoRepositoryImpl;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PedidoService {

    private PedidoRepository pedidoRepository = new PedidoRepositoryImpl();

    public List<Pedido> listarTodos() {
        return pedidoRepository.listarTodos();
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.buscarPorId(id);
    }

    public void salvar(Pedido pedido) {
        pedidoRepository.salvar(pedido);
    }

    public void deletar(Long id) {
        pedidoRepository.deletar(id);
    }

    public List<Pedido> buscarPorStatus(StatusPedido status) {
        return pedidoRepository.buscarPorStatus(status);
    }

    public void verificarAtrasos() {
        List<Pedido> pendentes = pedidoRepository.buscarPorStatus(StatusPedido.PENDENTE);
        for (Pedido pedido : pendentes) {
            if (pedido.getDataEnvio().isBefore(LocalDate.now())) {
                pedido.setStatus(StatusPedido.ATRASADO);
                pedidoRepository.salvar(pedido);
            }
        }
    }
    public List<Pedido> buscarEnviosDoMes() {
        LocalDate hoje = LocalDate.now();
        return pedidoRepository.listarTodos().stream()
                .filter(p -> p.getStatus() != StatusPedido.ATRASADO)
                .filter(p -> p.getDataEnvio().getMonth() == hoje.getMonth()
                        && p.getDataEnvio().getYear() == hoje.getYear())
                .collect(Collectors.toList());
    }
}