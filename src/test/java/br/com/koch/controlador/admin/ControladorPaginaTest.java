package br.com.koch.controlador.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ControladorPagina.class)
public class ControladorPaginaTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCenarioA_RedirecionamentosSimplesEAdministrativos() throws Exception {
        // Validação do redirecionamento para o painel administrativo
        mockMvc.perform(get("/painel"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/admin/dashboard"));

        // Validação do redirecionamento para a página inicial
        mockMvc.perform(get("/sobre"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void testCenarioB_RedirecionamentosComAncorasFragmentos() throws Exception {
        // Validação de atalhos institucionais com âncoras (fragmentos)
        mockMvc.perform(get("/nossa-historia"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/catalogo#historia"));

        mockMvc.perform(get("/historia"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/catalogo#historia"));

        mockMvc.perform(get("/contato"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/#localizacao"));
    }

    @Test
    public void testCenarioC_MapeamentoDeMúltiplosEndpoints() throws Exception {
        // Variações de rotas de catálogo/produtos
        mockMvc.perform(get("/suinos"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/catalogo#catalogo-produtos"));

        mockMvc.perform(get("/carnes"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/catalogo#catalogo-produtos"));

        // Variações de rotas de assinaturas/carrinho
        mockMvc.perform(get("/delivery"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/assinaturas"));

        mockMvc.perform(get("/carrinho"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/assinaturas"));
    }
}