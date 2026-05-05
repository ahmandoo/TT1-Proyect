package com.tt1.trabajo;

import com.tt1.trabajo.entity.CorreoEntity;
import com.tt1.trabajo.entity.UsuarioEntity;
import com.tt1.trabajo.repository.CorreoRepository;
import com.tt1.trabajo.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class CorreoRepositoryTest {

    @Autowired
    private CorreoRepository correoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        UsuarioEntity user1 = new UsuarioEntity("usuarioA");
        UsuarioEntity user2 = new UsuarioEntity("usuarioB");
        usuarioRepository.save(user1);
        usuarioRepository.save(user2);

        CorreoEntity correo1 = new CorreoEntity("1234", "usuarioA", "usuarioB", LocalDateTime.now(), "Mensaje 1");
        CorreoEntity correo2 = new CorreoEntity("1235", "usuarioB", "usuarioA", LocalDateTime.now(), "Mensaje 2");

        correoRepository.save(correo1);
        correoRepository.save(correo2);
    }

    @Test
    public void testFindByOrigenOrderByFechaDesc() {
        List<CorreoEntity> enviadosPorA = correoRepository.findByOrigenOrderByFechaDesc("usuarioA");
        assertNotNull(enviadosPorA);
        assertEquals(1, enviadosPorA.size(), "Debería haber un correo enviado por usuarioA");
        assertEquals("usuarioB", enviadosPorA.get(0).getDestino());
    }

    @Test
    public void testFindByDestinoOrderByFechaDesc() {
        List<CorreoEntity> recibidosPorA = correoRepository.findByDestinoOrderByFechaDesc("usuarioA");
        assertNotNull(recibidosPorA);
        assertEquals(1, recibidosPorA.size(), "Debería haber un correo recibido por usuarioA");
        assertEquals("usuarioB", recibidosPorA.get(0).getOrigen());
    }
}