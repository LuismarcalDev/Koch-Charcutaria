package br.com.koch.servico.admin;

import br.com.koch.modelo.admin.Pedido;
import br.com.koch.repositorio.admin.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

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
                .filter(p -> p.getAtivo())
                .collect(Collectors.toList());
    }

    public List<Pedido> buscarEnviosDoMes() {
        return pedidoRepository.findAll().stream()
                .filter(p -> p.getAtivo())
                .collect(Collectors.toList());
    }
}