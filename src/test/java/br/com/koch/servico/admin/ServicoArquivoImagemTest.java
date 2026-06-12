package br.com.koch.servico.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

/**
 * CT-14 — Validar regras de upload e validação de extensão em ServicoArquivoImagem
 * Tipo   : Unitário
 * Fase   : Verificação
 * Resp.  : Igor Farias
 */
class ServicoArquivoImagemTest {

    @TempDir
    Path diretorioTemporario;

    private ServicoArquivoImagem servicoArquivoImagem;

    @BeforeEach
    void configurar() {
        servicoArquivoImagem = new ServicoArquivoImagem(diretorioTemporario.toString());
    }

    // ------------------------------------------------------------------ //
    // Cenário A — upload com sucesso                                      //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioA_salvarImagemCesta_deveRetornarUrlCorretaParaArquivoPng() throws IOException {
        // Arrange
        MockMultipartFile arquivo = new MockMultipartFile(
                "imagem",
                "foto.png",
                "image/png",
                new byte[] { 1, 2, 3 }
        );

        // Act
        String url = servicoArquivoImagem.salvarImagemCesta(arquivo);

        // Assert
        assertNotNull(url);
        assertTrue(url.startsWith("/uploads/cestas/"),
                "A URL deve iniciar com /uploads/cestas/");
        assertTrue(url.endsWith(".png"),
                "A URL deve terminar com a extensão .png");
    }

    // ------------------------------------------------------------------ //
    // Cenário B — arquivo vazio ou nulo                                   //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioB_salvarImagemCesta_deveLancarExcecaoParaArquivoNulo() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> servicoArquivoImagem.salvarImagemCesta(null)
        );
        assertEquals("Selecione uma imagem para a cesta.", ex.getMessage());
    }

    @Test
    void cenarioB_salvarImagemCesta_deveLancarExcecaoParaArquivoVazio() {
        MockMultipartFile arquivoVazio = new MockMultipartFile(
                "imagem",
                "vazio.png",
                "image/png",
                new byte[] {}
        );

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> servicoArquivoImagem.salvarImagemCesta(arquivoVazio)
        );
        assertEquals("Selecione uma imagem para a cesta.", ex.getMessage());
    }

    // ------------------------------------------------------------------ //
    // Cenário C — formato de arquivo inválido                             //
    // ------------------------------------------------------------------ //

    @Test
    void cenarioC_salvarImagemCesta_deveLancarExcecaoParaContentTypeInvalido() {
        MockMultipartFile arquivoInvalido = new MockMultipartFile(
                "documento",
                "documento.pdf",
                "application/pdf",
                new byte[] { 1, 2, 3 }
        );

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> servicoArquivoImagem.salvarImagemCesta(arquivoInvalido)
        );
        assertEquals("O arquivo deve ser uma imagem.", ex.getMessage());
    }

    @Test
    void cenarioC_salvarImagemCesta_deveLancarExcecaoParaTextPlain() {
        MockMultipartFile arquivoTexto = new MockMultipartFile(
                "texto",
                "arquivo.txt",
                "text/plain",
                new byte[] { 1, 2, 3 }
        );

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> servicoArquivoImagem.salvarImagemCesta(arquivoTexto)
        );
        assertEquals("O arquivo deve ser uma imagem.", ex.getMessage());
    }
}