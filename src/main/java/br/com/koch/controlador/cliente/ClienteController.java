package br.com.koch.controlador.cliente;

import br.com.koch.dto.cliente.CadastroClienteRequest;
import br.com.koch.servico.cliente.ServicoCadastroCliente;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    private final ServicoCadastroCliente servicoCadastroCliente;

    public ClienteController(ServicoCadastroCliente servicoCadastroCliente) {
        this.servicoCadastroCliente = servicoCadastroCliente;
    }

    @GetMapping("/login")
    public String login() {
        return "cliente/login";
    }

    @GetMapping("/cadastro")
    public String cadastro(Model model) {
        model.addAttribute("cadastroClienteRequest", new CadastroClienteRequest());
        return "cliente/cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrar(
            @Valid @ModelAttribute CadastroClienteRequest cadastroClienteRequest,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "cliente/cadastro";
        }

        try {
            servicoCadastroCliente.cadastrar(cadastroClienteRequest);
        } catch (IllegalArgumentException ex) {
            bindingResult.rejectValue("email", "email.duplicado", ex.getMessage());
            return "cliente/cadastro";
        }

        return "redirect:/cliente/login?cadastroSucesso";
    }
}