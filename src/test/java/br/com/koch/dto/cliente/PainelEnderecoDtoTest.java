package br.com.koch.dto.cliente;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

/**
 * CT-10 — Validar Bean Validation (Caminho Feliz e Infeliz) do PainelEnderecoDto
 * Tipo   : Unitário
 * Fase   : Verificação
 * Resp.  : Igor Farias
 */
class PainelEnderecoDtoTest {

    private static Validator validator;

    @BeforeAll
    static void configurarValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void cenarioFeliz_enderecoValidoDeveGerarZeroViolacoes() {
        // Arrange
        PainelEnderecoDto dto = new PainelEnderecoDto();
        dto.setEndereco("Rua Alberto Nepomuceno, 123");

        // Act
        Set<ConstraintViolation<PainelEnderecoDto>> violacoes =
                validator.validate(dto);

        // Assert
        assertTrue(violacoes.isEmpty(),
                "Endereço válido não deve gerar nenhuma violação");
    }

    @Test
    void cenarioInfeliz_enderecoEmBrancoDeveGerarUmaViolacao() {
        // Arrange — @NotBlank dispara, @Size(max=255) não dispara para string vazia
        PainelEnderecoDto dto = new PainelEnderecoDto();
        dto.setEndereco("");

        // Act
        Set<ConstraintViolation<PainelEnderecoDto>> violacoes =
                validator.validate(dto);

        // Assert
        assertEquals(1, violacoes.size(),
                "Endereço em branco deve gerar exatamente 1 violação (@NotBlank)");
    }

    @Test
    void cenarioInfeliz_enderecoAcimaDe255CaracteresDeveGerarUmaViolacao() {
        // Arrange — string de 256 'a': @Size(max=255) dispara, @NotBlank não dispara
        String enderecoLongo = "a".repeat(256);
        PainelEnderecoDto dto = new PainelEnderecoDto();
        dto.setEndereco(enderecoLongo);

        // Act
        Set<ConstraintViolation<PainelEnderecoDto>> violacoes =
                validator.validate(dto);

        // Assert
        assertEquals(1, violacoes.size(),
                "Endereço com 256 caracteres deve gerar exatamente 1 violação (@Size)");
    }
}