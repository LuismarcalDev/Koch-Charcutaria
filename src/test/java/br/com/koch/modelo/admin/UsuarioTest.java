package br.com.koch.modelo.admin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

/**
 * CT-06 — Validar integridade, encapsulamento e variação de perfis da entidade Usuario
 * Tipo   : Unitário
 * Fase   : Verificação
 * Resp.  : Igor Farias
 */
class UsuarioTest {

    private static final UUID ID_ESPERADO =
            UUID.fromString("12345678-1234-1234-1234-123456789abc");

    @ParameterizedTest
    @EnumSource(Perfil.class)
    void deveArmazenarERecuperarAtributosParaCadaPerfil(Perfil perfil) {
        // Arrange
        Usuario usuario = new Usuario();

        // Act
        usuario.setId(ID_ESPERADO);
        usuario.setNome("Igor Farias");
        usuario.setEmail("igor@email.com");
        usuario.setSenhaHash("hfbsi2758b43gfv");
        usuario.setPerfil(perfil);

        // Assert
        assertEquals(ID_ESPERADO, usuario.getId());
        assertEquals("Igor Farias", usuario.getNome());
        assertEquals("igor@email.com", usuario.getEmail());
        assertEquals("hfbsi2758b43gfv", usuario.getSenhaHash());
        assertEquals(perfil, usuario.getPerfil());
    }

    @Test
    void deveCobrirTodosOsValoresDoEnumPerfil() {
        assertEquals(2, Perfil.values().length);
        assertNotNull(Perfil.valueOf("ADMINISTRADOR"));
        assertNotNull(Perfil.valueOf("CLIENTE"));
    }
}