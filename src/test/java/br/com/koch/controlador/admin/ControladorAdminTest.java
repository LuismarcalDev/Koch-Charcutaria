package br.com.koch.controlador.admin;

import br.com.koch.modelo.admin.Pedido;
import br.com.koch.modelo.admin.Produto;
import br.com.koch.modelo.cliente.Cliente;
import br.com.koch.servico.ClienteService;
import br.com.koch.servico.admin.PedidoService;
import br.com.koch.servico.admin.ProdutoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ControladorAdmin.class)
public class ControladorAdminTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProdutoService produtoService;

    @MockitoBean
    private PedidoService pedidoService;

    @MockitoBean
    private ClienteService clienteService;

    @Test
    public void testCenarioA_CarregamentoCompletoDoDashboard() throws Exception {
        // Criar coleções mockadas com as quantidades exatas exigidas no CT-31
        List<Produto> mockProdutos = Stream.generate(() -> Mockito.mock(Produto.class)).limit(3).toList();
        List<Pedido> mockPedidosTotais = Stream.generate(() -> Mockito.mock(Pedido.class)).limit(5).toList();
        List<Pedido> mockPedidosPendentes = Stream.generate(() -> Mockito.mock(Pedido.class)).limit(2).toList();
        List<Pedido> mockPedidosDoMes = Stream.generate(() -> Mockito.mock(Pedido.class)).limit(1).toList();
        List<Cliente> mockClientes = Stream.generate(() -> Mockito.mock(Cliente.class)).limit(4).toList();

        // Configurar o comportamento dos mocks dos serviços
        when(produtoService.listarTodos()).thenReturn(mockProdutos);
        when(pedidoService.listarTodos()).thenReturn(mockPedidosTotais);
        when(pedidoService.buscarPendentes()).thenReturn(mockPedidosPendentes);
        when(pedidoService.buscarEnviosDoMes()).thenReturn(mockPedidosDoMes);
        when(clienteService.listarTodos()).thenReturn(mockClientes);

        // Simular requisição HTTP GET para "/admin/dashboard" e validar status, view e injeção do Model
        mockMvc.perform(get("/admin/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/dashboard"))
                .andExpect(model().attributeExists("produtos", "pedidos", "pedidosPendentes", "pedidosDoMes", "clientes"))
                .andExpect(model().attribute("produtos", mockProdutos))
                .andExpect(model().attribute("pedidos", mockPedidosTotais))
                .andExpect(model().attribute("pedidosPendentes", mockPedidosPendentes))
                .andExpect(model().attribute("pedidosDoMes", mockPedidosDoMes))
                .andExpect(model().attribute("clientes", mockClientes));
    }
}