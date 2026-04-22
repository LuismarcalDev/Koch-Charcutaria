package br.com.koch.repositorio.cliente;

import br.com.koch.modelo.cliente.Cliente;

public interface RepositorioCliente {
    Cliente save(Cliente cliente);
}
