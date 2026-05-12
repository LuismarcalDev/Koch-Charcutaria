package br.com.koch.repositorio.cliente;

import br.com.koch.modelo.cliente.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositorioCliente extends JpaRepository<Cliente, Integer> {

    Optional<Cliente> findByUsuario_Email(String email);
}
