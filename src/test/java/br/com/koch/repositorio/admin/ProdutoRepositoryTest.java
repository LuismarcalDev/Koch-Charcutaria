package br.com.koch.repositorio.admin;

import br.com.koch.modelo.admin.Produto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

/**
 * CT-25 — Validar operações básicas de CRUD e mapeamento relacional em ProdutoRepository
 * Tipo   : Integração (Persistência)
 * Fase   : Verificação
 * Resp.  : Igor Farias
 */
@DataJpaTest
class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository produtoRepository;

    // ------------------------------------------------------------------ //
    // Cenário A — persistência e busca por ID                             //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioA_save_deveGerarIdEPersistirAtributosCorretamente() {
        // Arrange
        Produto produto = new Produto();
        produto.setNome("Cesta Premium");
        produto.setPreco(new BigDecimal("180.00"));
        produto.setImagemUrl("/uploads/cestas/premium.png");

        // Act
        Produto salvo = produtoRepository.save(produto);

        // Assert — ID gerado pelo banco
        assertNotNull(salvo.getId(),
                "O ID deve ser gerado automaticamente pelo banco");

        // Assert — recuperação e integridade dos atributos
        Optional<Produto> encontrado = produtoRepository.findById(salvo.getId());
        assertTrue(encontrado.isPresent(),
                "O produto deve ser encontrado pelo ID gerado");

        Produto recuperado = encontrado.get();
        assertEquals("Cesta Premium", recuperado.getNome());
        assertEquals(0, new BigDecimal("180.00").compareTo(recuperado.getPreco()));
        assertEquals("/uploads/cestas/premium.png", recuperado.getImagemUrl());
    }

    // ------------------------------------------------------------------ //
    // Cenário B — exclusão por ID                                         //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioB_deleteById_deveRemoverProdutoERetornarEmptyNaBuscaPosterior() {
        // Arrange — persiste o produto para então excluí-lo
        Produto produto = new Produto();
        produto.setNome("Cesta Premium");
        produto.setPreco(new BigDecimal("180.00"));
        produto.setImagemUrl("/uploads/cestas/premium.png");
        Produto salvo = produtoRepository.save(produto);
        Long idGerado = salvo.getId();

        // Act
        produtoRepository.deleteById(idGerado);

        // Assert
        Optional<Produto> resultado = produtoRepository.findById(idGerado);
        assertTrue(resultado.isEmpty(),
                "Após deleteById o produto não deve mais existir no banco");
    }
}