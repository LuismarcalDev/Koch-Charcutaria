package br.com.koch.dto.admin;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

/**
 * CT-07 — Validar Bean Validation (Caminho Feliz e Infeliz) do CadastroUsuarioRequest
 * Tipo   : Unitário
 * Fase   : Verificação
 * Resp.  : Igor Farias
 */
class CadastroUsuarioRequestTest {

    private static Validator validator;

    @BeforeAll
    static void configurarValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void cenarioFeliz_dadosValidosDevemGerarZeroViolacoes() {
        // Arrange
        CadastroUsuarioRequest request = new CadastroUsuarioRequest();
        request.setNome("Igor Farias");
        request.setEmail("igor@email.com");
        request.setSenha("senhaSegura123");

        // Act
        Set<ConstraintViolation<CadastroUsuarioRequest>> violacoes =
                validator.validate(request);

        // Assert
        assertTrue(violacoes.isEmpty(),
                "Dados válidos não devem gerar nenhuma violação");
    }

    @Test
    void cenarioInfeliz_dadosInvalidosDevemGerarTresViolacoes() {
        // Arrange — nome vazio, e-mail sem @, senha com menos de 6 caracteres
        CadastroUsuarioRequest request = new CadastroUsuarioRequest();
        request.setNome("");
        request.setEmail("igor_email.com");
        request.setSenha("123");

        // Act
        Set<ConstraintViolation<CadastroUsuarioRequest>> violacoes =
                validator.validate(request);

        // Assert
        assertEquals(3, violacoes.size(),
                "Devem ser detectadas exatamente 3 violações: nome, e-mail e senha");
    }
}