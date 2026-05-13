package com.tt1.trabajo;

import com.tt1.trabajo.controller.RegistroController;
import com.tt1.trabajo.entity.UsuarioEntity;
import com.tt1.trabajo.repository.UsuarioRepository;
import com.tt1.trabajo.repository.CorreoRepository; 
import com.tt1.trabajo.repository.SolicitudRepository; 
import interfaces.InterfazContactoSim; 
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegistroController.class)
public class RegistroControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioRepository usuarioRepository;
	
	@MockBean
    private CorreoRepository correoRepository;
	
    @MockBean
    private InterfazContactoSim icsMock;
	
    @MockBean
    private org.slf4j.Logger logger;
	
    @MockBean
    private com.tt1.trabajo.repository.SolicitudRepository solicitudRepository;
	
    @Test
    public void testShowRegistro() throws Exception {
        mockMvc.perform(get("/registro"))
                .andExpect(status().isOk())
                .andExpect(view().name("registro"));
    }

    @Test
    public void testRegistrarUsuarioNuevoExito() throws Exception {
        when(usuarioRepository.existsByUsername("nuevoUser")).thenReturn(false);

        mockMvc.perform(post("/registro")
                        .param("username", "nuevoUser")
                        .param("email", "nuevo@unirioja.es"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void testRegistrarUsuarioExistenteFalla() throws Exception {
        when(usuarioRepository.existsByUsername("userExistente")).thenReturn(true);

        mockMvc.perform(post("/registro")
                        .param("username", "userExistente")
                        .param("email", "existente@unirioja.es"))
                .andExpect(status().isOk())
                .andExpect(view().name("registro"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "El nombre de usuario ya existe."));
    }
}
