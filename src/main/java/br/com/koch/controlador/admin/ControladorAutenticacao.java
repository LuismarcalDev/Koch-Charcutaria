package br.com.koch.controlador.admin;



import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;



@Controller

public class ControladorAutenticacao {



    @GetMapping("/login")

    public String login() {

        return "admin/login";

    }



    /** Cadastro público de administrador desativado por segurança. */

    @GetMapping("/cadastro")

    public String cadastroDesabilitado() {

        return "redirect:/login";

    }



    @PostMapping("/cadastro")

    public String cadastroDesabilitadoPost() {

        return "redirect:/login";

    }

}

