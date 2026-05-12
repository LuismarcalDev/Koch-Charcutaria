package br.com.koch.repositorio.cliente;

import br.com.koch.modelo.cliente.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioCliente extends JpaRepository<Cliente, Integer> {
}
