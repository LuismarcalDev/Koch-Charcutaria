package br.com.koch.controlador.admin;

import br.com.koch.modelo.admin.Pedido;
import br.com.koch.servico.admin.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pedidos", pedidoService.listarTodos());
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/salvar")
    public String salvar(Pedido pedido) {
        pedidoService.salvar(pedido);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("pedido", pedidoService.buscarPorId(id));
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        pedidoService.deletar(id);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/check/{id}")
    public String marcarEnviado(@PathVariable Long id) {
        Pedido pedido = pedidoService.buscarPorId(id);
        pedido.setAtivo(false);
        pedidoService.salvar(pedido);
        return "redirect:/admin/dashboard";
    }
}
