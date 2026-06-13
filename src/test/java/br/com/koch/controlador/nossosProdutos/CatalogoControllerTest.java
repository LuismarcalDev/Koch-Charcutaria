package br.com.koch.controlador.nossosProdutos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Year;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CatalogoController.class)
public class CatalogoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCenariosA_B_C_D_ValidacaoCompletaCatalogo() throws Exception {
        mockMvc.perform(get("/catalogo"))
                .andExpect(status().isOk())
                .andExpect(view().name("nossosProdutos/catalogo"))
                // Validação de conteúdo institucional
                .andExpect(model().attribute("sobreTitulo", "Como Surgiu a Koch Charcutaria"))
                .andExpect(model().attribute("sobreTextos", hasSize(3)))
                .andExpect(model().attribute("bannerTexto", "Conheça algumas de nossas delícias"))
                // Validação da lista de produtos (Tamanho e nomes)
                .andExpect(model().attribute("produtos", hasSize(5)))
                .andExpect(model().attribute("produtos", hasItem(
                        allOf(hasProperty("nome", is("Panceta Defumada de Rolo")),
                                hasProperty("imagem", is("img1catalogo.jpeg"))))))
                .andExpect(model().attribute("produtos", hasItem(
                        allOf(hasProperty("nome", is("Pancetta Arrotolata"))))))
                .andExpect(model().attribute("produtos", hasItem(
                        allOf(hasProperty("nome", is("Copa Defumada"))))))
                .andExpect(model().attribute("produtos", hasItem(
                        allOf(hasProperty("nome", is("Toscana com queijo de coalho"))))))
                .andExpect(model().attribute("produtos", hasItem(
                        allOf(hasProperty("nome", is("Lombo defumado"))))))
                // Validação do ano dinâmico
                .andExpect(model().attribute("anoAtual", Year.now().getValue()));
    }
}