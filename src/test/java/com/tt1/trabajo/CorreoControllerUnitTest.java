package com.tt1.trabajo;

import com.tt1.trabajo.repository.CorreoRepository;
import com.tt1.trabajo.repository.UsuarioRepository;
import interfaces.InterfazContactoSim;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CorreoController.class)
public class CorreoControllerUnitTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private CorreoRepository correoRepository;
    @MockBean private UsuarioRepository usuarioRepository;
    @MockBean private InterfazContactoSim icsMock;
    @MockBean
    private com.tt1.trabajo.repository.SolicitudRepository solicitudRepository;
    @MockBean
    private org.slf4j.Logger logger;

    @Test
    public void testVerBandejaEntradaSinSesion() throws Exception {
        mockMvc.perform(get("/correos/bandeja"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void testVerBandejaEntradaConSesion() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "juan");

        when(correoRepository.findByDestinoOrderByFechaDesc("juan")).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/correos/bandeja").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("solicitud"))
                .andExpect(model().attributeExists("correos"));
    }

    @Test
    public void testProcesarEnvioDestinoNoExiste() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "origenUser");

        when(usuarioRepository.existsByUsername("destinoFalso")).thenReturn(false);

        mockMvc.perform(post("/correos/enviar")
                        .session(session)
                        .param("destino", "destinoFalso")
                        .param("mensaje", "Hola"))
                .andExpect(status().isOk())
                .andExpect(view().name("enviar_correo"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    public void testProcesarEnvioExitoso() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "origenUser");

        when(usuarioRepository.existsByUsername("destinoReal")).thenReturn(true);
        when(icsMock.solicitarSimulation(any(), anyString())).thenReturn(1001);

        mockMvc.perform(post("/correos/enviar")
                        .session(session)
                        .param("destino", "destinoReal")
                        .param("mensaje", "Hola amigo"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/correos/enviados"));
    }
}