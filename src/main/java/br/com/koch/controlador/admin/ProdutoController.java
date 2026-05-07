package br.com.koch.controlador.admin;


import br.com.koch.modelo.admin.Produto;
import br.com.koch.servico.admin.ProdutoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/produtos")
public class ProdutoController {

    private ProdutoService produtoService = new ProdutoService();

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("produtos", produtoService.listarTodos());
        return "admin/produtos";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("produto", new Produto());
        return "admin/produto-form";
    }

    @PostMapping("/salvar")
    public String salvar(Produto produto) {
        produtoService.salvar(produto);
        return "redirect:/admin/produtos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("produto", produtoService.buscarPorId(id));
        return "admin/produto-form";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return "redirect:/admin/produtos";
    }
}
