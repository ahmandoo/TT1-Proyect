package com.tt1.trabajo.controller;

import com.tt1.trabajo.entity.UsuarioEntity;
import com.tt1.trabajo.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * Controlador que gestiona la autenticación de los usuarios en el sistema,
 * abarcando las operaciones de inicio y cierre de sesión.
 */
@Controller
public class LoginController {
    private final UsuarioRepository userRepository;

    /**
     * Constructor para la inyección del repositorio de usuarios.
     * * @param repo Repositorio encargado de la persistencia y consulta de entidades {@link UsuarioEntity}.
     */
    public LoginController(UsuarioRepository repo) {
        this.userRepository = repo;
    }


    /**
     * Muestra la página principal de inicio de sesión.
     * * @param session Sesión HTTP actual.
     * @return Redirección a la vista de solicitudes ("/solicitud") si ya hay sesión iniciada, de lo contrario muestra la vista principal ("index").
     */
    @GetMapping("/")
    public String showLogin(HttpSession session) {
        if (session.getAttribute("username") != null) {
            return "redirect:/solicitud";
        }
        return "index";
    }
    /**
     * Procesa la solicitud de inicio de sesión. 
     * Verifica si el nombre de usuario existe en la base de datos; si no está registrado,
     * devuelve un mensaje de error solicitando el registro previo.
     * * @param username Nombre de usuario capturado desde el formulario.
     * @param session  Sesión HTTP donde se almacenará el atributo de identidad tras el éxito.
     * @param model    Modelo de Spring para pasar mensajes de error a la vista en caso de fallo.
     * @return Redirección a la vista de solicitudes ("/solicitud") si tiene éxito, o recarga la vista ("index") con un error si falla.
     */
    @PostMapping("/login")
    public String doLogin(@RequestParam String username, HttpSession session, Model model){
        UsuarioEntity usuario = userRepository.findByUsername(username).orElse(null);

        if (usuario == null) {
            model.addAttribute("error", "El usuario no está registrado. Por favor, regístrese primero.");
            return "index";
        }

        session.setAttribute("username", usuario.getUsername());

        return "redirect:/solicitud";
    }
    /**
     * Cierra la sesión del usuario actual invalidando su token de sesión.
     * Los datos del usuario se mantienen intactos en la base de datos.
     * * @param session Sesión HTTP a invalidar.
     * @return Redirección a la ruta principal ("/") tras efectuar el logout.
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
