package com.tt1.trabajo;

import com.tt1.trabajo.entity.UsuarioEntity;
import com.tt1.trabajo.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void testGuardarYBuscarUsuario() {
        UsuarioEntity nuevoUsuario = new UsuarioEntity("testUser");
        usuarioRepository.save(nuevoUsuario);
        Optional<UsuarioEntity> usuarioEncontrado = usuarioRepository.findByUsername("testUser");
        assertTrue(usuarioEncontrado.isPresent(), "El usuario debería existir en la base de datos");
        assertEquals("testUser", usuarioEncontrado.get().getUsername(), "El nombre de usuario debe coincidir");
        assertNotNull(usuarioEncontrado.get().getId(), "JPA debería haber generado un ID autoincremental");
    }

    @Test
    public void testBuscarUsuarioNoExistente() {
        Optional<UsuarioEntity> usuarioEncontrado = usuarioRepository.findByUsername("usuarioFantasma");
        assertFalse(usuarioEncontrado.isPresent(), "No debería encontrar un usuario que no existe");
    }
}