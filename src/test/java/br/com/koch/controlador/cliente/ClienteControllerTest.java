package br.com.koch.controlador.cliente;

import br.com.koch.dto.cliente.CadastroClienteRequest;
import org.springframework.security.test.context.support.WithMockUser;
import br.com.koch.dto.cliente.PainelUsuarioDto;
import br.com.koch.modelo.admin.Pedido;
import br.com.koch.modelo.admin.Usuario;
import br.com.koch.modelo.cliente.Cliente;
import br.com.koch.modelo.cliente.UsuarioCliente;
import br.com.koch.repositorio.cliente.RepositorioCliente;
import br.com.koch.servico.admin.ServicoAssinatura;
import br.com.koch.servico.cliente.ServicoCadastroCliente;
import br.com.koch.servico.cliente.ServicoPainelCliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ServicoCadastroCliente servicoCadastroCliente;
    @MockitoBean
    private RepositorioCliente repositorioCliente;
    @MockitoBean
    private ServicoPainelCliente servicoPainelCliente;
    @MockitoBean
    private ServicoAssinatura servicoAssinatura;

    @Test
    @WithMockUser(username = "cliente@email.com")
    public void testCenarioA_ExibicaoEMontagemDoPainel() throws Exception {
        Cliente mockCliente = new Cliente();
        mockCliente.setUsuario(new UsuarioCliente());
        when(repositorioCliente.findByUsuario_Email("cliente@email.com")).thenReturn(Optional.of(mockCliente));
        when(servicoAssinatura.listarPorCliente(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/cliente/painel"))
                .andExpect(status().isOk())
                .andExpect(view().name("cliente/painel"))
                .andExpect(model().attributeExists("usuario", "usuarioForm", "enderecoForm", "assinaturas"));
    }

    @Test
    @WithMockUser(username = "cliente@email.com")
    public void testCenarioB_AtualizacaoDadosPessoais() throws Exception {
        mockMvc.perform(post("/cliente/painel/dados").with(csrf())
                        .param("nome", "Novo Nome")
                        .param("telefone", "12345678"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/cliente/painel?aba=dados"))
                .andExpect(flash().attributeExists("msgDadosSalvos"));
    }

    @Test
    @WithMockUser(username = "cliente@email.com")
    public void testCenarioD_DesativacaoAssinatura() throws Exception {
        mockMvc.perform(post("/cliente/painel/assinaturas/1/desativar").with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/cliente/painel?aba=assinaturas"))
                .andExpect(flash().attributeExists("msgAssinaturaDesativada"));

        verify(servicoAssinatura).desativarAssinatura(1L, "cliente@email.com");
    }

    @Test
    public void testCenarioE_CadastroNovoClienteSucesso() throws Exception {
        mockMvc.perform(post("/cliente/cadastro").with(csrf())
                        .param("nome", "Teste")
                        .param("email", "novo@email.com")
                        .param("senha", "123456"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/cliente/login?cadastroSucesso"));

        verify(servicoCadastroCliente).cadastrar(any(CadastroClienteRequest.class));
    }

    @Test
    public void testCenarioE_EmailDuplicado() throws Exception {
        doThrow(new IllegalArgumentException("E-mail já existe")).when(servicoCadastroCliente).cadastrar(any());

        mockMvc.perform(post("/cliente/cadastro").with(csrf())
                        .param("nome", "Teste")
                        .param("email", "duplicado@email.com")
                        .param("senha", "123456"))
                .andExpect(status().isOk())
                .andExpect(view().name("cliente/cadastro"))
                .andExpect(model().hasErrors());
    }
}