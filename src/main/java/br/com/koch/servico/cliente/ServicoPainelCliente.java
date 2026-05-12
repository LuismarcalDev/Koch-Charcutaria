package br.com.koch.servico.cliente;

import br.com.koch.dto.cliente.PainelEnderecoDto;
import br.com.koch.dto.cliente.PainelUsuarioDto;
import br.com.koch.modelo.cliente.Cliente;
import br.com.koch.modelo.cliente.UsuarioCliente;
import br.com.koch.repositorio.cliente.RepositorioCliente;
import br.com.koch.repositorio.cliente.RepositorioUsuarioCliente;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicoPainelCliente {

    private final RepositorioCliente repositorioCliente;
    private final RepositorioUsuarioCliente repositorioUsuarioCliente;
    private final ServicoDetalhesCliente servicoDetalhesCliente;

    public ServicoPainelCliente(
            RepositorioCliente repositorioCliente,
            RepositorioUsuarioCliente repositorioUsuarioCliente,
            ServicoDetalhesCliente servicoDetalhesCliente
    ) {
        this.repositorioCliente = repositorioCliente;
        this.repositorioUsuarioCliente = repositorioUsuarioCliente;
        this.servicoDetalhesCliente = servicoDetalhesCliente;
    }

    @Transactional
    public void atualizarDados(String emailLogado, PainelUsuarioDto dto) {
        Cliente cliente = repositorioCliente.findByUsuario_Email(emailLogado)
                .orElseThrow(() -> new IllegalStateException("Cliente não encontrado."));
        UsuarioCliente usuario = cliente.getUsuario();

        String nome = dto.getNome() == null ? "" : dto.getNome().trim();
        String telefone = dto.getTelefone() == null ? "" : dto.getTelefone().trim();
        String novoEmail = dto.getEmail() == null ? "" : dto.getEmail().trim();

        if (nome.isEmpty() || telefone.isEmpty() || novoEmail.isEmpty()) {
            throw new IllegalArgumentException("Nome, telefone e e-mail são obrigatórios.");
        }

        String emailAtual = usuario.getEmail();
        if (!novoEmail.equalsIgnoreCase(emailAtual)) {
            repositorioUsuarioCliente.findByEmail(novoEmail).ifPresent(outro -> {
                if (!outro.getId().equals(usuario.getId())) {
                    throw new IllegalArgumentException("Este e-mail já está em uso por outra conta.");
                }
            });
            usuario.setEmail(novoEmail);
            repositorioUsuarioCliente.save(usuario);
        }

        cliente.setNome(nome);
        cliente.setTelefone(telefone);
        repositorioCliente.save(cliente);

        if (!novoEmail.equalsIgnoreCase(emailAtual)) {
            UserDetails detalhes = servicoDetalhesCliente.loadUserByUsername(novoEmail);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    detalhes,
                    detalhes.getPassword(),
                    detalhes.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
    }

    @Transactional
    public void atualizarEndereco(String emailLogado, PainelEnderecoDto dto) {
        Cliente cliente = repositorioCliente.findByUsuario_Email(emailLogado)
                .orElseThrow(() -> new IllegalStateException("Cliente não encontrado."));
        String endereco = dto.getEndereco() == null ? "" : dto.getEndereco().trim();
        if (endereco.isEmpty()) {
            throw new IllegalArgumentException("O endereço não pode ficar vazio.");
        }
        cliente.setEndereco(endereco);
        repositorioCliente.save(cliente);
    }
}
