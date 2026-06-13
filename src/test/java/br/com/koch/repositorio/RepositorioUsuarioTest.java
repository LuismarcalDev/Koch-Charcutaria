package br.com.koch.repositorio;

import br.com.koch.modelo.admin.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Optional;

public class RepositorioUsuarioTest {

    @Test
    public void testCenarioA_ContratoDeBuscaEExistencia() {
        try {
            // Validar buscarPorEmail
            Method buscarMethod = RepositorioUsuario.class.getMethod("buscarPorEmail", String.class);
            Assertions.assertEquals(Optional.class, buscarMethod.getReturnType(),
                    "Falha: O método buscarPorEmail deve retornar um Optional seguro para evitar NullPointerException.");

            // Validar existePorEmail
            Method existeMethod = RepositorioUsuario.class.getMethod("existePorEmail", String.class);
            Assertions.assertEquals(boolean.class, existeMethod.getReturnType(),
                    "Falha: O método existePorEmail deve retornar um boolean primitivo.");

        } catch (NoSuchMethodException e) {
            Assertions.fail("Falha estrutural: O método de busca ou verificação por e-mail não foi encontrado com a assinatura esperada.");
        }
    }

    @Test
    public void testCenarioB_ContratoDePersistencia() {
        try {
            // Validar salvar
            Method salvarMethod = RepositorioUsuario.class.getMethod("salvar", Usuario.class);
            Assertions.assertEquals(Usuario.class, salvarMethod.getReturnType(),
                    "Falha: O método salvar deve retornar a entidade Usuario de forma síncrona para permitir encadeamento.");

        } catch (NoSuchMethodException e) {
            Assertions.fail("Falha estrutural: O método 'salvar' recebendo 'Usuario' não foi encontrado na interface.");
        }
    }
}