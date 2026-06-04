package br.com.koch.configuracao;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.Year;

@ControllerAdvice
public class ModeloGlobalAdvice {

    @ModelAttribute("anoAtual")
    public int anoAtual() {
        return Year.now().getValue();
    }
}
