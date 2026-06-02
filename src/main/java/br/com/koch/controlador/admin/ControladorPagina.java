package br.com.koch.controlador.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorPagina {

    @GetMapping("/painel")
    public String painel() {
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/nossa-historia")
    public String historia() {
        return "redirect:/catalogo#historia";
    }

    @GetMapping("/contato")
    public String contato() {
        return "redirect:/#localizacao";
    }

    @GetMapping({"/suinos", "/carnes"})
    public String produtos() {
        return "redirect:/catalogo#catalogo-produtos";
    }

    @GetMapping("/historia")
    public String historiaLegado() {
        return "redirect:/catalogo#historia";
    }

    @GetMapping("/sobre")
    public String sobre() {
        return "redirect:/";
    }

    @GetMapping({"/delivery", "/carrinho"})
    public String assinaturasOuCarrinho() {
        return "redirect:/assinaturas";
    }
}
