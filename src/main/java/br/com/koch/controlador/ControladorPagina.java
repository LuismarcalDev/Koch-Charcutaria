package br.com.koch.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorPagina
{
    @GetMapping("/painel")
    public String painel()
    {
        return "painel";
    }
}