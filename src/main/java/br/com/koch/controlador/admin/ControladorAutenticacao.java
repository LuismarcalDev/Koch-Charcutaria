package br.com.koch.controlador.admin;

import br.com.koch.dto.admin.CadastroUsuarioRequest;
import br.com.koch.servico.ServicoCadastroUsuario;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ControladorAutenticacao
{
    private final ServicoCadastroUsuario servicoCadastroUsuario;

    public ControladorAutenticacao(ServicoCadastroUsuario servicoCadastroUsuario)
    {
        this.servicoCadastroUsuario = servicoCadastroUsuario;
    }

    @GetMapping("/login")
    public String login()
    {
        return "admin/login";
    }

    @GetMapping("/cadastro")
    public String abrirCadastro(Model model)
    {
        model.addAttribute("cadastroUsuarioRequest", new CadastroUsuarioRequest());
        return "admin/cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrar(
            @Valid @ModelAttribute CadastroUsuarioRequest cadastroUsuarioRequest,
            BindingResult bindingResult
    )
    {
        if (bindingResult.hasErrors())
        {
            return "admin/cadastro";
        }

        try
        {
            servicoCadastroUsuario.cadastrarAdministrador(cadastroUsuarioRequest);
        }
        catch (IllegalArgumentException ex)
        {
            bindingResult.rejectValue("email", "email.duplicado", ex.getMessage());
            return "admin/cadastro";
        }

        return "redirect:/login?cadastroSucesso";
    }
}