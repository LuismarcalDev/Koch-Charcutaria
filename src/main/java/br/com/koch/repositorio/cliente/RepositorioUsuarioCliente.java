package br.com.koch.repositorio.cliente;

import br.com.koch.modelo.cliente.UsuarioCliente;

import java.util.Optional;

public interface RepositorioUsuarioCliente {
    Optional<UsuarioCliente> findByEmail(String email);

    boolean existsByEmail(String email);

    UsuarioCliente save(UsuarioCliente usuario);
}
