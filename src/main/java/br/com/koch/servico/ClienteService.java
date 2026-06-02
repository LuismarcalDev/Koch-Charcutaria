package br.com.koch.servico;

import br.com.koch.modelo.cliente.Cliente;
import br.com.koch.repositorio.cliente.RepositorioCliente;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final RepositorioCliente repositorioCliente;

    public ClienteService(RepositorioCliente repositorioCliente) {
        this.repositorioCliente = repositorioCliente;
    }

    public List<Cliente> listarTodos() {
        return repositorioCliente.findAll();
    }
}
