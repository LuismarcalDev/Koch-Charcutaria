package br.com.koch.controlador.admin;

import br.com.koch.modelo.admin.Produto;
import br.com.koch.servico.admin.ProdutoService;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
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

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class, true));
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
            redirectAttributes.addFlashAttribute("abrirModalProduto", true);
        }
        return "redirect:/admin/dashboard?tab=criar";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id) {
        return "redirect:/admin/dashboard?tab=criar";
    }

    @PostMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            produtoService.deletar(id);
            redirectAttributes.addFlashAttribute("msgProdutoOk", "Cesta excluída com sucesso.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("msgProdutoErro", ex.getMessage());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("msgProdutoErro",
                    "Não foi possível excluir a cesta. Verifique se ainda há vínculos no banco.");
        }
        return "redirect:/admin/dashboard?tab=criar";
    }
}
