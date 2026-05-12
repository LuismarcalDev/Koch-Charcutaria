package br.com.koch.servico.cliente;

import br.com.koch.dto.cliente.CadastroClienteRequest;
import br.com.koch.modelo.cliente.Cliente;
import br.com.koch.modelo.cliente.UsuarioCliente;
import br.com.koch.repositorio.cliente.RepositorioCliente;
import br.com.koch.repositorio.cliente.RepositorioUsuarioCliente;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicoCadastroCliente {

    private final RepositorioUsuarioCliente repositorioUsuarioCliente;
    private final RepositorioCliente repositorioCliente;
    private final PasswordEncoder passwordEncoder;

    public ServicoCadastroCliente(
            RepositorioUsuarioCliente repositorioUsuarioCliente,
            RepositorioCliente repositorioCliente,
            PasswordEncoder passwordEncoder
    ) {
        this.repositorioUsuarioCliente = repositorioUsuarioCliente;
        this.repositorioCliente = repositorioCliente;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void cadastrar(CadastroClienteRequest request) {
        if (repositorioUsuarioCliente.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Já existe um cliente com esse e-mail.");
        }

        UsuarioCliente usuarioCliente = new UsuarioCliente();
        usuarioCliente.setTipo("CLIENTE");
        usuarioCliente.setEmail(request.getEmail());
        usuarioCliente.setSenha(passwordEncoder.encode(request.getSenha()));
        UsuarioCliente usuarioSalvo = repositorioUsuarioCliente.save(usuarioCliente);

        Cliente cliente = new Cliente();
        cliente.setNome(request.getNome());
        cliente.setTelefone(request.getTelefone());
        cliente.setEndereco(request.getEndereco());
        cliente.setUsuario(usuarioSalvo);
        repositorioCliente.save(cliente);
    }
}
