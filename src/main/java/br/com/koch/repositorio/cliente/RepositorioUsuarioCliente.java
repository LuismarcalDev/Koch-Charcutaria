package br.com.koch.repositorio.cliente;

import br.com.koch.modelo.cliente.UsuarioCliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepositorioUsuarioCliente extends JpaRepository<UsuarioCliente, Integer> {
    Optional<UsuarioCliente> findByEmail(String email);
    boolean existsByEmail(String email);
}
