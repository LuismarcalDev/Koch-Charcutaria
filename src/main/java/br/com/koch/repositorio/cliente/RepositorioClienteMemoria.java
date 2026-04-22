package br.com.koch.repositorio.cliente;

import br.com.koch.modelo.cliente.Cliente;
import org.springframework.stereotype.Repository;

import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class RepositorioClienteMemoria implements RepositorioCliente {

    private final AtomicInteger proximoId = new AtomicInteger(1);

    @Override
    public Cliente save(Cliente cliente) {
        if (cliente.getId() == null) {
            cliente.setId(proximoId.getAndIncrement());
        }
        return cliente;
    }
}
