package br.com.koch.controlador.cliente;

import br.com.koch.dto.cliente.CadastroClienteRequest;
import br.com.koch.dto.cliente.PainelAssinaturaDto;
import br.com.koch.dto.cliente.PainelEnderecoDto;
import br.com.koch.dto.cliente.PainelUsuarioDto;
import br.com.koch.modelo.cliente.Cliente;
import br.com.koch.repositorio.cliente.RepositorioCliente;
import br.com.koch.modelo.admin.Pedido;
import br.com.koch.servico.admin.ServicoAssinatura;
import br.com.koch.servico.cliente.ServicoCadastroCliente;
import br.com.koch.servico.cliente.ServicoPainelCliente;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    /** Cards de demonstração no painel (além das assinaturas reais do banco). */
    private static final List<PainelAssinaturaDto> ASSINATURAS_EXEMPLO = List.of(
            new PainelAssinaturaDto(
                    -1,
                    "Cesta Suínos (exemplo)",
                    "foto.png",
                    "Receba produtos selecionados todo mês com a qualidade Koch.",
                    new BigDecimal("29.00"),
                    "Mensal",
                    "Koch"
            )
    );

    private final ServicoCadastroCliente servicoCadastroCliente;
    private final RepositorioCliente repositorioCliente;
    private final ServicoPainelCliente servicoPainelCliente;
    private final ServicoAssinatura servicoAssinatura;

    public ClienteController(
            ServicoCadastroCliente servicoCadastroCliente,
            RepositorioCliente repositorioCliente,
            ServicoPainelCliente servicoPainelCliente,
            ServicoAssinatura servicoAssinatura
    ) {
        this.servicoCadastroCliente = servicoCadastroCliente;
        this.repositorioCliente = repositorioCliente;
        this.servicoPainelCliente = servicoPainelCliente;
        this.servicoAssinatura = servicoAssinatura;
    }

    @GetMapping("/login")
    public String login() {
        return "cliente/login";
    }

    @GetMapping("/painel")
    public String painel(Model model, Authentication authentication) {
        montarPainel(model, authentication.getName(), null, null);
        return "cliente/painel";
    }

    @PostMapping("/painel/dados")
    public String salvarDadosPessoais(
            @Valid @ModelAttribute("usuarioForm") PainelUsuarioDto usuarioForm,
            BindingResult bindingResult,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            montarPainel(model, authentication.getName(), usuarioForm, null);
            return "cliente/painel";
        }
        try {
            servicoPainelCliente.atualizarDados(authentication.getName(), usuarioForm);
            redirectAttributes.addFlashAttribute("msgDadosSalvos", true);
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("msgDadosErro", ex.getMessage());
        }
        return "redirect:/cliente/painel?aba=dados";
    }

    @PostMapping("/painel/endereco")
    public String salvarEndereco(
            @Valid @ModelAttribute("enderecoForm") PainelEnderecoDto enderecoForm,
            BindingResult bindingResult,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            montarPainel(model, authentication.getName(), null, enderecoForm);
            return "cliente/painel";
        }
        try {
            servicoPainelCliente.atualizarEndereco(authentication.getName(), enderecoForm);
            redirectAttributes.addFlashAttribute("msgEnderecoSalvo", true);
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("msgEnderecoErro", ex.getMessage());
        }
        return "redirect:/cliente/painel?aba=enderecos";
    }

    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        model.addAttribute("cadastroClienteRequest", new CadastroClienteRequest());
        return "cliente/cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrar(
            @Valid @ModelAttribute CadastroClienteRequest cadastroClienteRequest,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "cliente/cadastro";
        }

        try {
            servicoCadastroCliente.cadastrar(cadastroClienteRequest);
        } catch (IllegalArgumentException ex) {
            bindingResult.rejectValue("email", "email.duplicado", ex.getMessage());
            return "cliente/cadastro";
        }

        return "redirect:/cliente/login?cadastroSucesso";
    }

    private void montarPainel(
            Model model,
            String emailAutenticado,
            PainelUsuarioDto usuarioFormOverride,
            PainelEnderecoDto enderecoFormOverride
    ) {
        Cliente cliente = repositorioCliente.findByUsuario_Email(emailAutenticado)
                .orElseThrow(() -> new IllegalStateException("Cliente não encontrado para o usuário autenticado."));

        PainelUsuarioDto usuario = painelUsuarioDtoDe(cliente);
        model.addAttribute("usuario", usuario);

        PainelUsuarioDto usuarioForm = usuarioFormOverride != null
                ? usuarioFormOverride
                : painelUsuarioDtoDe(cliente);
        model.addAttribute("usuarioForm", usuarioForm);

        PainelEnderecoDto enderecoForm = enderecoFormOverride != null
                ? enderecoFormOverride
                : new PainelEnderecoDto(cliente.getEndereco());
        model.addAttribute("enderecoForm", enderecoForm);

        List<PainelAssinaturaDto> assinaturas = new ArrayList<>(
                servicoAssinatura.listarPorCliente(cliente.getId()).stream()
                        .filter(p -> Boolean.TRUE.equals(p.getAtivo()))
                        .map(ClienteController::painelAssinaturaDtoDe)
                        .collect(Collectors.toList())
        );
        assinaturas.addAll(ASSINATURAS_EXEMPLO);
        model.addAttribute("assinaturas", assinaturas);
    }

    private static PainelAssinaturaDto painelAssinaturaDtoDe(Pedido pedido) {
        String imagem = pedido.getProduto() != null && pedido.getProduto().getImagemUrl() != null
                ? pedido.getProduto().getImagemUrl()
                : "/cliente/img/foto.png";
        String descricao = pedido.getProduto() != null && pedido.getProduto().getDescricao() != null
                ? pedido.getProduto().getDescricao()
                : "Assinatura Koch Charcutaria.";
        return new PainelAssinaturaDto(
                pedido.getId().intValue(),
                pedido.getNomeAssinatura(),
                imagem,
                descricao,
                pedido.getPreco(),
                "Mensal",
                "Koch"
        );
    }

    private static PainelUsuarioDto painelUsuarioDtoDe(Cliente cliente) {
        PainelUsuarioDto dto = new PainelUsuarioDto();
        dto.setNome(cliente.getNome());
        dto.setTelefone(cliente.getTelefone());
        dto.setEmail(cliente.getUsuario().getEmail());
        dto.setEndereco(cliente.getEndereco());
        return dto;
    }
}
