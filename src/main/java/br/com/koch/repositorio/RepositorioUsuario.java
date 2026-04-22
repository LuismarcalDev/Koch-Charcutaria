package br.com.koch.repositorio;

import br.com.koch.modelo.Usuario;

import java.util.Optional;

public interface RepositorioUsuario
{
    Optional<Usuario> buscarPorEmail(String email);

    boolean existePorEmail(String email);

    Usuario salvar(Usuario usuario);
}