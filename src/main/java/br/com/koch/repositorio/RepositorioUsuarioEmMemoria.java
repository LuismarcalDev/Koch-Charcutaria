package br.com.koch.repositorio;

import br.com.koch.modelo.Usuario;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class RepositorioUsuarioEmMemoria implements RepositorioUsuario
{
    private final Map<String, Usuario> usuariosPorEmail = new ConcurrentHashMap<>();

    @Override
    public Optional<Usuario> buscarPorEmail(String email)
    {
        return Optional.ofNullable(usuariosPorEmail.get(email.toLowerCase()));
    }

    @Override
    public boolean existePorEmail(String email)
    {
        return usuariosPorEmail.containsKey(email.toLowerCase());
    }

    @Override
    public Usuario salvar(Usuario usuario)
    {
        usuariosPorEmail.put(usuario.getEmail().toLowerCase(), usuario);
        return usuario;
    }
}