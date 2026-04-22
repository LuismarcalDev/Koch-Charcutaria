package br.com.koch.servico.cliente;

import br.com.koch.modelo.cliente.UsuarioCliente;
import br.com.koch.repositorio.cliente.RepositorioUsuarioCliente;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicoDetalhesCliente implements UserDetailsService {

    private final RepositorioUsuarioCliente repositorioUsuarioCliente;

    public ServicoDetalhesCliente(RepositorioUsuarioCliente repositorioUsuarioCliente) {
        this.repositorioUsuarioCliente = repositorioUsuarioCliente;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UsuarioCliente usuarioCliente = repositorioUsuarioCliente.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente não encontrado."));

        String papel = usuarioCliente.getTipo() == null || usuarioCliente.getTipo().isBlank()
                ? "CLIENTE"
                : usuarioCliente.getTipo().trim().toUpperCase();

        return new User(
                usuarioCliente.getEmail(),
                usuarioCliente.getSenha(),
                List.of(new SimpleGrantedAuthority("ROLE_" + papel))
        );
    }
}
