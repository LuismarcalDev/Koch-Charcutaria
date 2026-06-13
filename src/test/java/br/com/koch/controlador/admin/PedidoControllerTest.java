package br.com.koch.controlador.admin;

import br.com.koch.modelo.admin.Pedido;
import br.com.koch.servico.admin.PedidoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoController.class)
public class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PedidoService pedidoService;

    @Test
    public void testCenarioA_ListagemComRedirecionamento() throws Exception {
        when(pedidoService.listarTodos()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/admin/pedidos"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/admin/dashboard"));

        verify(pedidoService).listarTodos();
    }

    @Test
    public void testCenarioB_SalvarNovoPedido() throws Exception {
        mockMvc.perform(post("/admin/pedidos/salvar")
                        .param("id", "1")
                        .param("ativo", "true"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/admin/dashboard"));

        verify(pedidoService).salvar(any(Pedido.class));
    }

    @Test
    public void testCenarioC_PrepararEdicaoPorId() throws Exception {
        when(pedidoService.buscarPorId(1L)).thenReturn(new Pedido());

        mockMvc.perform(get("/admin/pedidos/editar/1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/admin/dashboard"));

        verify(pedidoService).buscarPorId(1L);
    }

    @Test
    public void testCenarioD_RemocaoDeRegistro() throws Exception {
        mockMvc.perform(get("/admin/pedidos/deletar/1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/admin/dashboard"));

        verify(pedidoService).deletar(1L);
    }

    @Test
    public void testCenarioE_ConcluirDarBaixaNoPedido() throws Exception {
        Pedido pedido = new Pedido();
        pedido.setAtivo(true);
        when(pedidoService.buscarPorId(1L)).thenReturn(pedido);

        mockMvc.perform(post("/admin/pedidos/check/1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/admin/dashboard"));

        // Verifica se o estado foi alterado para false e se o método salvar foi invocado
        verify(pedidoService).salvar(argThat(p -> !p.getAtivo()));
    }
}