package br.com.koch.servico.admin;

import br.com.koch.modelo.admin.Pedido;
import br.com.koch.repositorio.admin.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id).orElse(null);
    }

    public void salvar(Pedido pedido) {
        pedidoRepository.save(pedido);
    }

    public void deletar(Long id) {
        pedidoRepository.deleteById(id);
    }

    public List<Pedido> buscarPendentes() {
        return pedidoRepository.findAll().stream()
                .filter(p -> Boolean.TRUE.equals(p.getAtivo()))
                .collect(Collectors.toList());
    }

    public List<Pedido> buscarEnviosDoMes() {
        return buscarPendentes();
    }
}
