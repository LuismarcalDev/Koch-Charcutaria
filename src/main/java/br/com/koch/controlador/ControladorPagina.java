package br.com.koch.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorPagina {

    @GetMapping("/painel")
    public String painel() {
        return "painel";
    }

    @GetMapping("/nossa-historia")
    public String historia() {
        return "redirect:/catalogo";
    }

    @GetMapping("/contato")
    public String contato() {
        return "redirect:/";
    }
}