package br.com.koch.controlador.assinaturas;

import br.com.koch.modelo.cliente.Cliente;
import br.com.koch.repositorio.cliente.RepositorioCliente;
import br.com.koch.servico.admin.ProdutoService;
import br.com.koch.servico.admin.ServicoAssinatura;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.Map;

@Controller
@RequestMapping("/assinaturas")
public class AssinaturasController {

    private final ProdutoService produtoService;
    private final ServicoAssinatura servicoAssinatura;
    private final RepositorioCliente repositorioCliente;

    public AssinaturasController(
            ProdutoService produtoService,
            ServicoAssinatura servicoAssinatura,
            RepositorioCliente repositorioCliente
    ) {
        this.produtoService = produtoService;
        this.servicoAssinatura = servicoAssinatura;
        this.repositorioCliente = repositorioCliente;
    }

    @GetMapping
    public String assinaturas(Model model, Authentication authentication) {
        model.addAttribute("cestas", produtoService.listarTodos());
        model.addAttribute("assinaturasAtivasPorProduto", mapaAssinaturasDoCliente(authentication));
        return "assinaturas/assinaturas";
    }

    @PostMapping("/{produtoId}/assinar")
    public String assinar(
            @PathVariable Long produtoId,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/cliente/login?redirect=/assinaturas";
        }

        if (isAdministrador(authentication)) {
            redirectAttributes.addFlashAttribute("msgAssinaturaErro",
                    "Use a área do cliente para assinar uma cesta.");
            return "redirect:/assinaturas";
        }

        try {
            Long assinaturaId = servicoAssinatura.assinarCesta(produtoId, authentication.getName());
            redirectAttributes.addFlashAttribute("msgAssinaturaNova", true);
            return "redirect:/cliente/painel?aba=assinaturas&assinatura=" + assinaturaId;
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("msgAssinaturaErro", ex.getMessage());
            return servicoAssinatura.buscarIdAssinaturaAtivaPorProduto(authentication.getName(), produtoId)
                    .map(id -> "redirect:/cliente/painel?aba=assinaturas&assinatura=" + id)
                    .orElse("redirect:/assinaturas");
        }
    }

    private Map<Long, Long> mapaAssinaturasDoCliente(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() || isAdministrador(authentication)) {
            return Collections.emptyMap();
        }
        return repositorioCliente.findByUsuario_Email(authentication.getName())
                .map(Cliente::getId)
                .map(servicoAssinatura::mapaAssinaturasAtivasPorProduto)
                .orElse(Collections.emptyMap());
    }

    private static boolean isAdministrador(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMINISTRADOR".equals(a.getAuthority()));
    }
}
