package br.com.koch.repositorio;

import br.com.koch.modelo.admin.ClienteListar;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    public void testCenarioA_PersistenciaEBuscaPorId() {
        // Inicializar entidade conforme Cenário A
        ClienteListar cliente = new ClienteListar();
        cliente.setNome("Carlos Souza");
        cliente.setTelefone("45999887766");
        // Opcional: cliente.setEndereco("Endereço de Teste");

        // Salvar e confirmar geração do ID
        ClienteListar clienteSalvo = clienteRepository.save(cliente);
        Assertions.assertNotNull(clienteSalvo.getId(), "Falha: O ID do cliente não foi gerado após o save().");

        // Buscar via findById()
        Optional<ClienteListar> clienteBuscado = clienteRepository.findById(clienteSalvo.getId());

        // Atestar que os dados foram gravados perfeitamente
        Assertions.assertTrue(clienteBuscado.isPresent(), "Falha: Cliente não encontrado no banco após inserção.");
        Assertions.assertEquals("Carlos Souza", clienteBuscado.get().getNome(), "Falha: Nome gravado incorretamente.");
        Assertions.assertEquals("45999887766", clienteBuscado.get().getTelefone(), "Falha: Telefone gravado incorretamente.");
    }

    @Test
    public void testCenarioB_ListagemGeral() {
        // Pré-condição: Inserir um registro para o teste de findAll()
        ClienteListar cliente = new ClienteListar();
        cliente.setNome("Carlos Souza");
        cliente.setTelefone("45999887766");
        clienteRepository.save(cliente);

        // Executar o método findAll()
        List<ClienteListar> clientesListados = clienteRepository.findAll();

        // Garantir que o registro está presente na lista retornada
        Assertions.assertFalse(clientesListados.isEmpty(), "Falha: A lista retornada pelo findAll() está vazia.");
        Assertions.assertTrue(clientesListados.size() >= 1, "Falha: A lista deveria conter pelo menos o registro recém-criado.");

        // Valida se o registro inserido é o que consta na lista
        boolean clienteEncontrado = clientesListados.stream()
                .anyMatch(c -> "Carlos Souza".equals(c.getNome()));
        Assertions.assertTrue(clienteEncontrado, "Falha: O cliente 'Carlos Souza' não foi encontrado na listagem geral.");
    }
}