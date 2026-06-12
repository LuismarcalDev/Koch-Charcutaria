package br.com.koch.servico;

import br.com.koch.modelo.cliente.Cliente;
import br.com.koch.modelo.cliente.UsuarioCliente;
import br.com.koch.repositorio.cliente.RepositorioCliente;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * CT-21 — Validar listagem completa de clientes em ClienteService
 * Tipo   : Unitário
 * Fase   : Verificação
 * Resp.  : Igor Farias
 */
@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private RepositorioCliente repositorioCliente;

    @InjectMocks
    private ClienteService clienteService;

    // ------------------------------------------------------------------ //
    // Cenário A — listagem com sucesso                                    //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioA_listarTodos_deveRetornarListaComTodosOsClientes() {
        // Arrange
        UsuarioCliente usuario1 = new UsuarioCliente();
        usuario1.setId(1);
        usuario1.setEmail("cliente1@email.com");

        Cliente cliente1 = new Cliente();
        cliente1.setId(1);
        cliente1.setNome("Ana Silva");
        cliente1.setUsuario(usuario1);

        UsuarioCliente usuario2 = new UsuarioCliente();
        usuario2.setId(2);
        usuario2.setEmail("cliente2@email.com");

        Cliente cliente2 = new Cliente();
        cliente2.setId(2);
        cliente2.setNome("Igor Farias");
        cliente2.setUsuario(usuario2);

        when(repositorioCliente.findAll())
                .thenReturn(List.of(cliente1, cliente2));

        // Act
        List<Cliente> resultado = clienteService.listarTodos();

        // Assert
        assertNotNull(resultado,
                "A lista retornada não deve ser nula");
        assertEquals(2, resultado.size(),
                "A lista deve conter exatamente 2 clientes");
        assertEquals(1, resultado.get(0).getId());
        assertEquals("Ana Silva", resultado.get(0).getNome());
        assertEquals(2, resultado.get(1).getId());
        assertEquals("Igor Farias", resultado.get(1).getNome());
        verify(repositorioCliente, times(1)).findAll();
    }
}