package com.tt1.trabajo;

import com.tt1.trabajo.entity.UsuarioEntity;
import com.tt1.trabajo.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void testDoLogin_UsuarioExiste() throws Exception {
        UsuarioEntity usuarioExistente = new UsuarioEntity("usuarioRegistrado");
        usuarioExistente.setEmail("test@unirioja.es");
        usuarioRepository.save(usuarioExistente);

        mockMvc.perform(post("/login")
                        .param("username", "usuarioRegistrado"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/solicitud"));
    }

    @Test
    public void testDoLogin_UsuarioNoExiste() throws Exception {
        mockMvc.perform(post("/login")
                        .param("username", "usuarioFantasma"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("error"));
    }
}