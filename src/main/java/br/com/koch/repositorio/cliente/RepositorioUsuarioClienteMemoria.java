package br.com.koch.repositorio.cliente;

import br.com.koch.modelo.cliente.UsuarioCliente;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class RepositorioUsuarioClienteMemoria implements RepositorioUsuarioCliente {

    private final Map<String, UsuarioCliente> porEmail = new ConcurrentHashMap<>();
    private final AtomicInteger proximoId = new AtomicInteger(1);

    @Override
    public Optional<UsuarioCliente> findByEmail(String email) {
        return Optional.ofNullable(porEmail.get(normalizar(email)));
    }

    @Override
    public boolean existsByEmail(String email) {
        return porEmail.containsKey(normalizar(email));
    }

    @Override
    public UsuarioCliente save(UsuarioCliente usuario) {
        if (usuario.getId() == null) {
            usuario.setId(proximoId.getAndIncrement());
        }
        porEmail.put(normalizar(usuario.getEmail()), usuario);
        return usuario;
    }

    private static String normalizar(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }
}
