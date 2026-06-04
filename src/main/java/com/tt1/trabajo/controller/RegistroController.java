package com.tt1.trabajo.controller;

import com.tt1.trabajo.entity.UsuarioEntity;
import com.tt1.trabajo.repository.UsuarioRepository;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * Controlador encargado de gestionar el registro de nuevos usuarios en el sistema.
 */
@Controller
public class RegistroController {

    private final UsuarioRepository userRepository;
    /**
     * Constructor para inyectar el repositorio de usuarios.
     *
     * @param UsRepo Repositorio de la entidad de usuarios utilizado para interactuar con la base de datos.
     */
    public RegistroController(UsuarioRepository UsRepo) {
        this.userRepository = UsRepo;
    }
    /**
     * Maneja la petición GET para mostrar la vista del formulario de registro.
     *
     * @return El nombre de la plantilla HTML correspondiente al registro ("registro").
     */
    @GetMapping("/registro")
    public String showRegistro() {
        return "registro";
    }
    /**
     * Maneja la petición POST para procesar el registro de un nuevo usuario.
     * Verifica si el nombre de usuario ya existe; si es así, devuelve un error.
     * En caso contrario, crea y guarda el usuario en la base de datos.
     *
     * @param username Nombre de usuario introducido en el formulario.
     * @param email    Correo electrónico introducido en el formulario.
     * @param model    Modelo de Spring para pasar mensajes de error a la vista en caso de fallo.
     * @return Redirección a la vista principal ("index") si el registro es exitoso,
     * o recarga la vista de registro ("registro") mostrando un mensaje de error si el usuario ya existe.
     */
    @PostMapping("/registro")
    public String registrarUsuario(@RequestParam String username, @RequestParam String email, Model model) {

        if (userRepository.existsByUsername(username)) {
            model.addAttribute("error", "El nombre de usuario ya existe.");
            return "registro";
        }

        UsuarioEntity nuevoUsuario = new UsuarioEntity(username);
        nuevoUsuario.setEmail(email);
        userRepository.save(nuevoUsuario);

        return "index";
    }
}
