package com.tt1.trabajo;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * Controlador que gestiona la autenticación básica de los usuarios (inicio y cierre de sesión).
 */
@Controller
public class LoginController {
    /**
     * Muestra la página principal de inicio de sesión.
     * * @param session Sesión HTTP actual.
     * @return Redirección a la vista de solicitudes si ya hay sesión iniciada, de lo contrario muestra "index".
     */
    @GetMapping("/")
    public String showLogin(HttpSession session) {
        if (session.getAttribute("username") != null) {
            return "redirect:/solicitud";
        }
        return "index";
    }
    /**
     * Procesa el formulario de inicio de sesión y almacena al usuario en la sesión.
     * * @param username Nombre de usuario introducido en el formulario.
     * @param session  Sesión HTTP donde se guardará el usuario.
     * @return Redirección a la ruta "/solicitud".
     */
    @PostMapping("/login")
    public String doLogin(@RequestParam String username, HttpSession session){
        session.setAttribute("username", username);
        return "redirect:/solicitud";
    }
    /**
     * Cierra la sesión del usuario actual y elimina sus datos.
     * * @param session Sesión HTTP a invalidar.
     * @return Redirección a la ruta principal ("/") tras hacer logout.
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
