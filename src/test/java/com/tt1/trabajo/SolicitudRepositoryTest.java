package com.tt1.trabajo;

import com.tt1.trabajo.entity.SolicitudEntity;
import com.tt1.trabajo.entity.UsuarioEntity;
import com.tt1.trabajo.repository.SolicitudRepository;
import com.tt1.trabajo.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class SolicitudRepositoryTest {

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private UsuarioEntity testUser;

    @BeforeEach
    void setUp() {
        testUser = new UsuarioEntity("simuUser");
        usuarioRepository.save(testUser);
    }

    @Test
    public void testGuardarYBuscarSolicitudesPorUsuario() {
        SolicitudEntity solicitud1 = new SolicitudEntity(1001, testUser, "PENDIENTE");
        SolicitudEntity solicitud2 = new SolicitudEntity(1002, testUser, "COMPLETADA");

        solicitudRepository.save(solicitud1);
        solicitudRepository.save(solicitud2);
        List<SolicitudEntity> solicitudes = solicitudRepository.findByUsuarioUsername("simuUser");
        assertNotNull(solicitudes);
        assertEquals(2, solicitudes.size(), "Debería encontrar las 2 solicitudes creadas");
    }

    @Test
    public void testBuscarSolicitudPorToken() {
        SolicitudEntity solicitud = new SolicitudEntity(2001, testUser, "PROCESANDO");
        solicitudRepository.save(solicitud);
        SolicitudEntity encontrada = solicitudRepository.findById(2001).orElse(null);
        assertNotNull(encontrada);
        assertEquals("PROCESANDO", encontrada.getEstado());
        assertEquals(testUser.getUsername(), encontrada.getUsuario().getUsername());
    }
}