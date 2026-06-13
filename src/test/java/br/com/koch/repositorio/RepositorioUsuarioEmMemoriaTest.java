package br.com.koch.repositorio;

import br.com.koch.modelo.admin.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

public class RepositorioUsuarioEmMemoriaTest {

    private RepositorioUsuarioEmMemoria repositorio;

    @BeforeEach
    public void setUp() {
        repositorio = new RepositorioUsuarioEmMemoria();
    }

    @Test
    public void testCenarioA_NormalizacaoDeCaixaEPersistencia() {
        // Criar o objeto Usuario conforme dados do Cenário A
        Usuario usuario = new Usuario();
        usuario.setId(UUID.randomUUID());
        usuario.setNome("Igor Farias");
        usuario.setEmail("Igor.Adm@KOCH.com");
        usuario.setSenhaHash("$2a$10$...");

        // Salvar no repositório em memória
        repositorio.salvar(usuario);

        // Buscar o usuário usando o e-mail todo em minúsculas
        Optional<Usuario> usuarioBuscado = repositorio.buscarPorEmail("igor.adm@koch.com");

        // Certificar-se de que o registro foi encontrado e os dados estão íntegros
        Assertions.assertTrue(usuarioBuscado.isPresent(),
                "Falha: O usuário deveria ter sido encontrado usando o e-mail em letras minúsculas.");
        Assertions.assertEquals("Igor Farias", usuarioBuscado.get().getNome(),
                "Falha: O nome do usuário retornado está incorreto.");
        Assertions.assertEquals("Igor.Adm@KOCH.com", usuarioBuscado.get().getEmail(),
                "Falha: O e-mail original armazenado foi alterado indevidamente.");
    }

    @Test
    public void testCenarioB_VerificacaoDeExistencia() {
        // Pré-condição: Cadastrar o usuário do cenário anterior
        Usuario usuario = new Usuario();
        usuario.setId(UUID.randomUUID());
        usuario.setNome("Igor Farias");
        usuario.setEmail("Igor.Adm@KOCH.com");
        repositorio.salvar(usuario);

        // Validar que existePorEmail retorna true para variações de maiúsculas/minúsculas
        boolean existeCadastradoMinusculo = repositorio.existePorEmail("igor.adm@koch.com");
        boolean existeCadastradoOriginal = repositorio.existePorEmail("Igor.Adm@KOCH.com");
        boolean existeCadastradoMaiusculo = repositorio.existePorEmail("IGOR.ADM@KOCH.COM");

        Assertions.assertTrue(existeCadastradoMinusculo,
                "Falha: existePorEmail deveria retornar true para o e-mail em minúsculas.");
        Assertions.assertTrue(existeCadastradoOriginal,
                "Falha: existePorEmail deveria retornar true para o e-mail na grafia original.");
        Assertions.assertTrue(existeCadastradoMaiusculo,
                "Falha: existePorEmail deveria retornar true para o e-mail em maiúsculas.");

        // Validar que retorna false para um e-mail ausente no mapa
        boolean naoExiste = repositorio.existePorEmail("nao_cadastrado@koch.com");
        Assertions.assertFalse(naoExiste,
                "Falha: existePorEmail deveria retornar false para um e-mail não cadastrado.");
    }
}