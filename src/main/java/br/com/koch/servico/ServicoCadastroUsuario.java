package br.com.koch.servico;

import br.com.koch.dto.CadastroUsuarioRequest;
import br.com.koch.modelo.Perfil;
import br.com.koch.modelo.Usuario;
import br.com.koch.repositorio.RepositorioUsuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ServicoCadastroUsuario
{
    private final RepositorioUsuario repositorioUsuario;
    private final PasswordEncoder passwordEncoder;

    public ServicoCadastroUsuario(RepositorioUsuario repositorioUsuario, PasswordEncoder passwordEncoder)
    {
        this.repositorioUsuario = repositorioUsuario;
        this.passwordEncoder = passwordEncoder;
    }

    public void cadastrarAdministrador(CadastroUsuarioRequest request)
    {
        if (repositorioUsuario.existePorEmail(request.getEmail()))
        {
            throw new IllegalArgumentException("Já existe um usuário com esse e-mail.");
        }

        Usuario usuario = new Usuario(
                UUID.randomUUID(),
                request.getNome(),
                request.getEmail(),
                passwordEncoder.encode(request.getSenha()),
                Perfil.ADMINISTRADOR
        );

        repositorioUsuario.salvar(usuario);
    }
}