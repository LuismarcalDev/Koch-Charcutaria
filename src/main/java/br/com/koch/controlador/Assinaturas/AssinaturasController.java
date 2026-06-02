package br.com.koch.controlador.Assinaturas;

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

@Controller
@RequestMapping("/assinaturas")
public class AssinaturasController {

    private final ProdutoService produtoService;
    private final ServicoAssinatura servicoAssinatura;

    public AssinaturasController(ProdutoService produtoService, ServicoAssinatura servicoAssinatura) {
        this.produtoService = produtoService;
        this.servicoAssinatura = servicoAssinatura;
    }

    @GetMapping
    public String assinaturas(Model model) {
        model.addAttribute("cestas", produtoService.listarTodos());
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

        if (authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMINISTRADOR".equals(a.getAuthority()))) {
            redirectAttributes.addFlashAttribute("msgAssinaturaErro",
                    "Use a área do cliente para assinar uma cesta.");
            return "redirect:/assinaturas";
        }

        try {
            servicoAssinatura.assinarCesta(produtoId, authentication.getName());
            redirectAttributes.addFlashAttribute("msgAssinaturaOk",
                    "Assinatura realizada com sucesso! Confira em Minha conta.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("msgAssinaturaErro", ex.getMessage());
        }

        return "redirect:/assinaturas";
    }
}
