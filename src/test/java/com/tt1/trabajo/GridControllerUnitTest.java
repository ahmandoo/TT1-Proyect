package com.tt1.trabajo;

import interfaces.InterfazContactoSim;
import modelo.DatosSimulation;
import modelo.Punto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GridController.class)
public class GridControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InterfazContactoSim icsMock;
    @MockBean
    private Logger loggerMock;
    @MockBean
    private com.tt1.trabajo.repository.UsuarioRepository usuarioRepository;
    @MockBean
    private com.tt1.trabajo.repository.SolicitudRepository solicitudRepository;

    @Test
    public void testGetGridSinSesionRedirigeLogin() throws Exception {
        mockMvc.perform(get("/grid").param("tok", "12345"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void testGetGridConSesionTransformaColoresCorrectamente() throws Exception {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("username", "usuarioPrueba");
        DatosSimulation dsMock = new DatosSimulation();
        dsMock.setAnchoTablero(10);
        dsMock.setMaxSegundos(1);
        Punto p = new Punto();
        p.setX(5);
        p.setY(3);
        p.setColor("#FF0000");
        dsMock.setPuntos(Map.of(0, List.of(p)));
        when(icsMock.descargarDatos(12345, "usuarioPrueba")).thenReturn(dsMock);
        mockMvc.perform(get("/grid").param("tok", "12345").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("grid"))
                .andExpect(model().attribute("count", 10))
                .andExpect(model().attributeExists("maxTime"))
                .andExpect(model().attributeExists("colors"));
    }
}
