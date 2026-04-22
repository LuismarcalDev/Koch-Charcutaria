package br.com.koch.servico;

import br.com.koch.modelo.Usuario;
import br.com.koch.repositorio.RepositorioUsuario;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoDetalhesUsuario implements UserDetailsService
{
    private final RepositorioUsuario repositorioUsuario;

    public ServicoDetalhesUsuario(RepositorioUsuario repositorioUsuario)
    {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
    {
        Usuario usuario = repositorioUsuario.buscarPorEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));

        return new User(
                usuario.getEmail(),
                usuario.getSenhaHash(),
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getPerfil().name()))
        );
    }
}