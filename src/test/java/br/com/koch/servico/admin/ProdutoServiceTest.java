package br.com.koch.servico.admin;

import br.com.koch.modelo.admin.Produto;
import br.com.koch.repositorio.admin.PedidoRepository;
import br.com.koch.repositorio.admin.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * CT-13 — Validar lógica de negócio, tratamento de exceções e upload em ProdutoService
 * Tipo   : Unitário
 * Fase   : Verificação
 * Resp.  : Igor Farias
 */
@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ServicoArquivoImagem servicoArquivoImagem;

    @InjectMocks
    private ProdutoService produtoService;

    // ------------------------------------------------------------------ //
    // Cenário A — salvar com imagem válida                                //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioA_salvar_devePersistirProdutoComUrlDaImagem() throws IOException {
        // Arrange
        Produto produto = new Produto();
        produto.setNome("Cesta Colonial");
        produto.setPreco(new BigDecimal("120.00"));

        MultipartFile imagemMock = mock(MultipartFile.class);
        when(imagemMock.isEmpty()).thenReturn(false);
        when(servicoArquivoImagem.salvarImagemCesta(imagemMock))
                .thenReturn("/uploads/cestas/cesta-colonial.jpg");

        // Act
        produtoService.salvar(produto, imagemMock);

        // Assert
        assertEquals("/uploads/cestas/cesta-colonial.jpg", produto.getImagemUrl(),
                "A URL da imagem deve ser atribuída ao produto após o upload");
        verify(servicoArquivoImagem, times(1)).salvarImagemCesta(imagemMock);
        verify(produtoRepository, times(1)).save(produto);
    }

    // ------------------------------------------------------------------ //
    // Cenário B — validação de dados ao salvar                           //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioB_salvar_deveLancarExcecaoQuandoNomeEmBranco() {
        // Arrange
        Produto produto = new Produto();
        produto.setNome("");
        produto.setPreco(new BigDecimal("120.00"));

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> produtoService.salvar(produto, null)
        );
        assertEquals("Informe o nome da cesta.", ex.getMessage());
        verify(produtoRepository, never()).save(any());
    }

    @Test
    void cenarioB_salvar_deveLancarExcecaoQuandoPrecoNulo() {
        // Arrange
        Produto produto = new Produto();
        produto.setNome("Cesta Colonial");
        produto.setPreco(null);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> produtoService.salvar(produto, null)
        );
        assertEquals("Informe o preço da cesta.", ex.getMessage());
        verify(produtoRepository, never()).save(any());
    }

    // ------------------------------------------------------------------ //
    // Cenário C — exclusão de registro inexistente                        //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioC_deletar_deveLancarExcecaoQuandoIdNaoExiste() {
        // Arrange
        when(produtoRepository.existsById(99L)).thenReturn(false);

        // Act + Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> produtoService.deletar(99L)
        );
        assertEquals("Cesta não encontrada.", ex.getMessage());
        verify(pedidoRepository, never()).deleteByProduto_Id(any());
        verify(produtoRepository, never()).deleteById(any());
    }
}