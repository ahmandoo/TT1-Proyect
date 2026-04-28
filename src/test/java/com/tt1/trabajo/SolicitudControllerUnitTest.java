package com.tt1.trabajo;

import interfaces.InterfazContactoSim;
import modelo.Entidad;
import com.tt1.trabajo.entity.UsuarioEntity;
import com.tt1.trabajo.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SolicitudController.class)
public class SolicitudControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InterfazContactoSim icsMock;
    @MockBean
    private UsuarioRepository usuarioRepository;

    @MockBean
    private com.tt1.trabajo.repository.SolicitudRepository solicitudRepository;

    @Test
    public void testGetSolicitudRedirigeSiNoHaySesion() throws Exception {
        mockMvc.perform(get("/solicitud"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void testGetSolicitudMuestraFormularioConSesion() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "usuarioPrueba");

        Entidad e1 = new Entidad();
        e1.setId(1);
        e1.setName("Coches");
        when(icsMock.getEntities()).thenReturn(List.of(e1));

        mockMvc.perform(get("/solicitud").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("solicitud"))
                .andExpect(model().attributeExists("entities"));
    }

    @Test
    public void testPostSolicitudConExito() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "usuarioPrueba");
        UsuarioEntity usuarioMock = new UsuarioEntity("usuarioPrueba");

        when(usuarioRepository.findByUsername("usuarioPrueba"))
                .thenReturn(Optional.of(usuarioMock));

        when(icsMock.isValidEntityId(1)).thenReturn(true);
        when(icsMock.solicitarSimulation(any(), eq("usuarioPrueba"))).thenReturn(999);


        mockMvc.perform(post("/solicitud")
                        .session(session)
                        .param("1", "5"))
                .andExpect(status().isOk())
                .andExpect(view().name("formResult"))
                .andExpect(model().attribute("token", 999));
    }
}