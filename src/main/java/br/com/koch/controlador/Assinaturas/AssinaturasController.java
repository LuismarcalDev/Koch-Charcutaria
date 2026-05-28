package br.com.koch.controlador.Assinaturas;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.Year;

@Controller
@RequestMapping("/assinaturas")
public class AssinaturasController {

    @GetMapping
    public String assinaturas(Model model) {
        model.addAttribute("anoAtual", Year.now().getValue());
        return "assinaturas/assinaturas";
    }
}