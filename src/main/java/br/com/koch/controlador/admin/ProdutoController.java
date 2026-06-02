package br.com.koch.controlador.admin;

import br.com.koch.modelo.admin.Produto;
import br.com.koch.servico.admin.ProdutoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/admin/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public String listar() {
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/novo")
    public String novo() {
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/salvar")
    public String salvar(
            @ModelAttribute Produto produto,
            @RequestParam(value = "imagemArquivo", required = false) org.springframework.web.multipart.MultipartFile imagemArquivo,
            RedirectAttributes redirectAttributes
    ) {
        try {
            if (produto.getId() == null && (imagemArquivo == null || imagemArquivo.isEmpty())) {
                throw new IllegalArgumentException("Envie uma imagem para a cesta.");
            }
            produtoService.salvar(produto, imagemArquivo);
            redirectAttributes.addFlashAttribute("msgProdutoOk", "Cesta salva com sucesso.");
        } catch (IllegalArgumentException | IOException ex) {
            redirectAttributes.addFlashAttribute("msgProdutoErro", ex.getMessage());
        }
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id) {
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return "redirect:/admin/dashboard";
    }
}
