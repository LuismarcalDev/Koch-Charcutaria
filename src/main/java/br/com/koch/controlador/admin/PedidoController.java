package br.com.koch.controlador.admin;

import br.com.koch.modelo.admin.Pedido;
import br.com.koch.modelo.admin.StatusPedido;
import br.com.koch.servico.admin.PedidoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/pedidos")
public class PedidoController {

    private PedidoService pedidoService = new PedidoService();

    @GetMapping
    public String listar(Model model) {
        pedidoService.verificarAtrasos();
        model.addAttribute("pedidos", pedidoService.listarTodos());
        return "admin/pedidos";
    }

    @PostMapping("/salvar")
    public String salvar(Pedido pedido) {
        pedidoService.salvar(pedido);
        return "redirect:/admin/pedidos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("pedido", pedidoService.buscarPorId(id));
        return "admin/pedido-form";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        pedidoService.deletar(id);
        return "redirect:/admin/pedidos";
    }

    @PostMapping("/check/{id}")
    public String marcarEnviado(@PathVariable Long id) {
        Pedido pedido = pedidoService.buscarPorId(id);
        pedido.setStatus(StatusPedido.ENVIADO);
        pedidoService.salvar(pedido);
        return "redirect:/admin/pedidos";
    }
}
