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
 * CT-08 — Validar Bean Validation (Caminho Feliz e Infeliz) do CadastroClienteRequest
 * Tipo   : Unitário
 * Fase   : Verificação
 * Resp.  : Igor Farias
 */
class CadastroClienteRequestTest {

    private static Validator validator;

    @BeforeAll
    static void configurarValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void cenarioFeliz_dadosValidosDevemGerarZeroViolacoes() {
        // Arrange
        CadastroClienteRequest request = new CadastroClienteRequest();
        request.setNome("Ana Silva");
        request.setEmail("ana@email.com");
        request.setTelefone("45988888888");
        request.setEndereco("Avenida Brasil, 456");
        request.setSenha("senhaCliente123");

        // Act
        Set<ConstraintViolation<CadastroClienteRequest>> violacoes =
                validator.validate(request);

        // Assert
        assertTrue(violacoes.isEmpty(),
                "Dados válidos não devem gerar nenhuma violação");
    }

    @Test
    void cenarioInfeliz_dadosInvalidosDevemGerarCincoViolacoes() {
        // Arrange — nome/telefone/endereco/senha em branco, e-mail sem @
        CadastroClienteRequest request = new CadastroClienteRequest();
        request.setNome("");
        request.setEmail("ana_email.com");
        request.setTelefone("");
        request.setEndereco("");
        request.setSenha("");

        // Act
        Set<ConstraintViolation<CadastroClienteRequest>> violacoes =
                validator.validate(request);

        // Assert
        assertEquals(5, violacoes.size(),
                "Devem ser detectadas exatamente 5 violações: nome, e-mail, telefone, endereco e senha");
    }
}