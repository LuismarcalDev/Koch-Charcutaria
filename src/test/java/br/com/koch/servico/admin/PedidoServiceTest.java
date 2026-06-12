package br.com.koch.servico.admin;

import br.com.koch.modelo.admin.Pedido;
import br.com.koch.repositorio.admin.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * CT-12 — Validar lógica de negócio e isolamento da classe PedidoService
 * Tipo   : Unitário
 * Fase   : Verificação
 * Resp.  : Igor Farias
 */
@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void cenarioA_buscarPendentes_deveRetornarApenasAtivos() {
        // Arrange
        Pedido pedidoAtivo = new Pedido();
        pedidoAtivo.setId(1L);
        pedidoAtivo.setAtivo(true);

        Pedido pedidoInativo = new Pedido();
        pedidoInativo.setId(2L);
        pedidoInativo.setAtivo(false);

        when(pedidoRepository.findAll())
                .thenReturn(List.of(pedidoAtivo, pedidoInativo));

        // Act
        List<Pedido> resultado = pedidoService.buscarPendentes();

        // Assert
        assertEquals(1, resultado.size(),
                "buscarPendentes() deve retornar apenas pedidos com ativo=true");
        assertTrue(resultado.get(0).getAtivo(),
                "O pedido retornado deve ter ativo=true");
        assertEquals(1L, resultado.get(0).getId());
        verify(pedidoRepository, times(1)).findAll();
    }

    @Test
    void cenarioB_buscarPorId_deveRetornarPedidoQuandoIdExiste() {
        // Arrange
        Pedido pedidoEsperado = new Pedido();
        pedidoEsperado.setId(1L);
        pedidoEsperado.setAtivo(true);

        when(pedidoRepository.findById(1L))
                .thenReturn(Optional.of(pedidoEsperado));

        // Act
        Pedido resultado = pedidoService.buscarPorId(1L);

        // Assert
        assertNotNull(resultado, "Deve retornar o pedido quando o ID existe");
        assertEquals(1L, resultado.getId());
        verify(pedidoRepository, times(1)).findById(1L);
    }

    @Test
    void cenarioB_buscarPorId_deveRetornarNullQuandoIdNaoExiste() {
        // Arrange
        when(pedidoRepository.findById(99L))
                .thenReturn(Optional.empty());

        // Act
        Pedido resultado = pedidoService.buscarPorId(99L);

        // Assert
        assertNull(resultado, "Deve retornar null quando o ID não existe");
        verify(pedidoRepository, times(1)).findById(99L);
    }
}