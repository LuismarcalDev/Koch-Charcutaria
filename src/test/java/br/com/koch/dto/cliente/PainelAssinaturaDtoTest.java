package br.com.koch.dto.cliente;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

/**
 * CT-09 — Validar integridade e construção do DTO imutável PainelAssinaturaDto
 * Tipo   : Unitário
 * Fase   : Verificação
 * Resp.  : Igor Farias
 */
class PainelAssinaturaDtoTest {

    @Test
    void deveConstruirDtoImutavelERecuperarAtributosCorretamente() {
        // Arrange
        BigDecimal precoEsperado = new BigDecimal("89.90");

        // Act
        PainelAssinaturaDto dto = new PainelAssinaturaDto(
                1L,
                "Plano Premium",
                "salame.png",
                "Assinatura de embutidos artesanais",
                precoEsperado,
                "Mensal",
                "Koch Charcutaria"
        );

        // Assert
        assertEquals(1L, dto.getId());
        assertEquals("Plano Premium", dto.getNome());
        assertEquals("salame.png", dto.getFoto());
        assertEquals("Assinatura de embutidos artesanais", dto.getDescricao());
        assertEquals(0, precoEsperado.compareTo(dto.getPreco()));
        assertEquals("Mensal", dto.getPeriodicidade());
        assertEquals("Koch Charcutaria", dto.getFornecedor());
    }

    @Test
    void deveConfirmarImutabilidadeDoDto() {
        // Verifica que a classe não expõe nenhum setter público
        long contadorSetters = java.util.Arrays.stream(PainelAssinaturaDto.class.getMethods())
                .filter(m -> m.getName().startsWith("set"))
                .count();

        assertEquals(0, contadorSetters,
                "Um DTO imutável não deve expor nenhum setter público");
    }
}