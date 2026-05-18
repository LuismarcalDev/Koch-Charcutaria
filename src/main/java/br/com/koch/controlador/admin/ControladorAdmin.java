package br.com.koch.controlador.admin;

import br.com.koch.servico.admin.PedidoService;
import br.com.koch.servico.admin.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import br.com.koch.servico.ClienteService;

@Controller
public class ControladorAdmin {

    @Autowired
    private ProdutoService produtoService = new ProdutoService();
    @Autowired
    private PedidoService pedidoService = new PedidoService();

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("produtos", produtoService.listarTodos());
        model.addAttribute("pedidos", pedidoService.buscarPendentes());
        model.addAttribute("pedidosPendentes", pedidoService.buscarPendentes());
        model.addAttribute("pedidosDoMes", pedidoService.buscarEnviosDoMes());
        model.addAttribute("clientes", clienteService.listarTodos());
        return "admin/dashboard";
    }



}