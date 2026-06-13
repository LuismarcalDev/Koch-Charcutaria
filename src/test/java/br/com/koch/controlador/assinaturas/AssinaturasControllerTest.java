package br.com.koch.controlador.assinaturas;

import br.com.koch.servico.admin.ProdutoService;
import br.com.koch.servico.admin.ServicoAssinatura;
import br.com.koch.repositorio.cliente.RepositorioCliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.authorization.AuthenticatedAuthorizationManager.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AssinaturasController.class)
public class AssinaturasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProdutoService produtoService;

    @MockitoBean
    private ServicoAssinatura servicoAssinatura;

    @MockitoBean
    private RepositorioCliente repositorioCliente;

    @Test
    @WithMockUser(username = "comprador@email.com", roles = "CLIENTE")
    public void testCenarioA_ExibicaoPaginaAssinaturas() throws Exception {
        mockMvc.perform(get("/assinaturas"))
                .andExpect(status().isOk())
                .andExpect(view().name("assinaturas/assinaturas"))
                .andExpect(model().attributeExists("cestas", "assinaturasAtivasPorProduto"));
    }

    @Test
    public void testCenarioB_BloqueioUsuarioNaoAutenticado() throws Exception {

        mockMvc.perform(post("/assinaturas/1/assinar")
                .with(org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/cliente/login?redirect=/assinaturas"));
    }

    @Test
    @WithMockUser(username = "admin@koch.com", roles = "ADMINISTRADOR")
    public void testCenarioC_TentativaAssinaturaPorAdministrador() throws Exception {
        mockMvc.perform(post("/assinaturas/1/assinar"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/assinaturas"))
                .andExpect(flash().attributeExists("msgAssinaturaErro"));
    }

    @Test
    @WithMockUser(username = "comprador@email.com", roles = "CLIENTE")
    public void testCenarioD_AssinaturaComSucesso() throws Exception {
        when(servicoAssinatura.assinarCesta(1L, "comprador@email.com")).thenReturn(99L);

        mockMvc.perform(post("/assinaturas/1/assinar"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/cliente/painel?aba=assinaturas&assinatura=99"))
                .andExpect(flash().attribute("msgAssinaturaNova", true));
    }

    @Test
    @WithMockUser(username = "comprador@email.com", roles = "CLIENTE")
    public void testCenarioE_TratamentoAssinaturaDuplicada() throws Exception {
        // Simula erro de duplicidade e recuperação do ID existente
        when(servicoAssinatura.assinarCesta(1L, "comprador@email.com"))
                .thenThrow(new IllegalArgumentException("Já assinada"));
        when(servicoAssinatura.buscarIdAssinaturaAtivaPorProduto("comprador@email.com", 1L))
                .thenReturn(Optional.of(55L));

        mockMvc.perform(post("/assinaturas/1/assinar"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/cliente/painel?aba=assinaturas&assinatura=55"))
                .andExpect(flash().attributeExists("msgAssinaturaErro"));
    }
}