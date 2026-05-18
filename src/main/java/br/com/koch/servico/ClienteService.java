package br.com.koch.servico;

import br.com.koch.modelo.admin.ClienteListar;
import br.com.koch.repositorio.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<ClienteListar> listarTodos() {
        return clienteRepository.findAll();
    }
}
