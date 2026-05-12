package br.com.koch.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class InitialPageContoller {
    @GetMapping("/") // Este comando diz: "Leia este método primeiro ao acessar a URL base"
    public String home() {
        return "initial_page";
    }
}
