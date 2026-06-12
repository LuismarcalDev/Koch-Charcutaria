package br.com.koch.modelo.admin;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;
/**
 * CT-03 — Validar integridade e encapsulamento da entidade Produto
 * Tipo   : Unitário
 * Fase   : Verificação
 * Resp.  : Igor Farias
 */

class ProdutoTest {

    @Test
    void deveArmazenarERecuperarAtributosCorretamente() {
        // Arrange
        Produto produto = new Produto();
        BigDecimal precoEsperado = new BigDecimal("45.90");

        // Act
        produto.setId(1L);
        produto.setNome("Copa Defumada");
        produto.setDescricao("Copa artesanal curada por 60 dias");
        produto.setImagemUrl("copa.jpg");
        produto.setPreco(precoEsperado);

        // Assert
        assertEquals(1L, produto.getId());
        assertEquals("Copa Defumada", produto.getNome());
        assertEquals("Copa artesanal curada por 60 dias", produto.getDescricao());
        assertEquals("copa.jpg", produto.getImagemUrl());
        assertEquals(0, precoEsperado.compareTo(produto.getPreco()));
    }
}