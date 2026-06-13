package br.com.koch.controlador.admin;

import br.com.koch.dto.admin.CadastroUsuarioRequest;
import br.com.koch.servico.ServicoCadastroUsuario;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ControladorAutenticacao.class)
public class ControladorAutenticacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ServicoCadastroUsuario servicoCadastroUsuario;

    @Test
    public void testCenarioA_ExibicaoDaTelaDeLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/login"));
    }

    @Test
    public void testCenarioB_ExibicaoDaTelaDeCadastro() throws Exception {
        mockMvc.perform(get("/cadastro"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/cadastro"))
                .andExpect(model().attributeExists("cadastroUsuarioRequest"));
    }

    @Test
    public void testCenarioC_CadastroComSucesso() throws Exception {
        // Envio com senha "123456" para passar pela restrição @Size(min = 6) do DTO
        mockMvc.perform(post("/cadastro")
                        .param("nome", "Igor")
                        .param("email", "novo@koch.com")
                        .param("senha", "123456"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login?cadastroSucesso"));

        // Confirmar que o serviço foi invocado
        verify(servicoCadastroUsuario).cadastrarAdministrador(any(CadastroUsuarioRequest.class));
    }

    @Test
    public void testCenarioD_FalhaDeValidacaoJSR303() throws Exception {
        // Envio de POST com campos nulos/inválidos
        mockMvc.perform(post("/cadastro")
                        .param("nome", "") // Inválido (@NotBlank)
                        .param("email", "email_invalido") // Inválido (@Email)
                        .param("senha", "123")) // Inválido (@Size min=6)
                .andExpect(status().isOk())
                .andExpect(view().name("admin/cadastro"))
                .andExpect(model().hasErrors());

        // Garantir que a falha impediu a chamada ao serviço
        verify(servicoCadastroUsuario, never()).cadastrarAdministrador(any(CadastroUsuarioRequest.class));
    }

    @Test
    public void testCenarioE_TratamentoDeExcecaoDeEmailDuplicado() throws Exception {
        // Configurar o serviço para lançar a exceção simulando e-mail existente
        doThrow(new IllegalArgumentException("E-mail já cadastrado"))
                .when(servicoCadastroUsuario).cadastrarAdministrador(any(CadastroUsuarioRequest.class));

        // Realizar o POST com dados válidos estruturalmente, mas que causarão a exceção no serviço
        mockMvc.perform(post("/cadastro")
                        .param("nome", "Igor")
                        .param("email", "existente@koch.com")
                        .param("senha", "123456"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/cadastro"))
                .andExpect(model().attributeHasFieldErrorCode("cadastroUsuarioRequest", "email", "email.duplicado"));
    }
}