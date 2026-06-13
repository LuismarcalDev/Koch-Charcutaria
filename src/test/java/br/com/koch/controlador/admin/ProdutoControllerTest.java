package br.com.koch.controlador.admin;

import br.com.koch.modelo.admin.Produto;
import br.com.koch.servico.admin.ProdutoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProdutoController.class)
public class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProdutoService produtoService;

    @Test
    public void testCenarioA_RedirecionamentosIniciais() throws Exception {
        mockMvc.perform(get("/admin/produtos"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/admin/dashboard"));

        mockMvc.perform(get("/admin/produtos/novo"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/admin/dashboard"));
    }

    @Test
    public void testCenarioB_SalvarNovoProdutoComSucesso() throws Exception {
        MockMultipartFile file = new MockMultipartFile("imagemArquivo", "foto.jpg", "image/jpeg", "content".getBytes());

        mockMvc.perform(multipart("/admin/produtos/salvar")
                        .file(file)
                        .param("nome", "Cesta Suína")
                        .param("preco", "150.0"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/admin/dashboard?tab=criar"))
                .andExpect(flash().attributeExists("msgProdutoOk"));

        verify(produtoService).salvar(any(Produto.class), any());
    }

    @Test
    public void testCenarioC_BloqueioDeCadastroSemImagem() throws Exception {
        mockMvc.perform(multipart("/admin/produtos/salvar")
                        .param("nome", "Cesta Falha"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/admin/dashboard?tab=criar"))
                .andExpect(flash().attributeExists("msgProdutoErro", "abrirModalProduto"));
    }

    @Test
    public void testCenarioD_ExclusaoSeguraDeRegistro() throws Exception {
        mockMvc.perform(post("/admin/produtos/deletar/1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/admin/dashboard?tab=criar"))
                .andExpect(flash().attribute("msgProdutoOk", "Cesta excluída com sucesso."));

        verify(produtoService).deletar(1L);
    }

    @Test
    public void testCenarioE_TratamentoDeErrosPorVinculosRelacionais() throws Exception {
        doThrow(new RuntimeException("Violação de FK")).when(produtoService).deletar(1L);

        mockMvc.perform(post("/admin/produtos/deletar/1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/admin/dashboard?tab=criar"))
                .andExpect(flash().attributeExists("msgProdutoErro"));

        // Verifica que o erro amigável foi enviado ao invés do stacktrace
        verify(produtoService).deletar(1L);
    }
}