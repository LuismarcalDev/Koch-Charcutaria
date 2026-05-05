package br.com.koch.servico.admin;

import br.com.koch.dto.admin.CadastroUsuarioRequest;
import br.com.koch.modelo.admin.Perfil;
import br.com.koch.modelo.admin.Usuario;
import br.com.koch.repositorio.RepositorioUsuario;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ServicoCadastroUsuarioAdm
{
    private final RepositorioUsuario repositorioUsuario;
    private final PasswordEncoder passwordEncoder;

    public ServicoCadastroUsuarioAdm(RepositorioUsuario repositorioUsuario, PasswordEncoder passwordEncoder)
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