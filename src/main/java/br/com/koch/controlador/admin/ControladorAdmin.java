package br.com.koch.controlador.admin;

import br.com.koch.modelo.admin.StatusPedido;
import br.com.koch.servico.admin.PedidoService;
import br.com.koch.servico.admin.ProdutoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControladorAdmin {

    private ProdutoService produtoService = new ProdutoService();
    private PedidoService pedidoService = new PedidoService();

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        pedidoService.verificarAtrasos();
        model.addAttribute("produtos", produtoService.listarTodos());
        model.addAttribute("pedidos", pedidoService.listarTodos());
        model.addAttribute("pedidosPendentes", pedidoService.buscarPorStatus(StatusPedido.PENDENTE));
        model.addAttribute("pedidosDoMes", pedidoService.buscarEnviosDoMes());
        return "admin/dashboard";
    }


}