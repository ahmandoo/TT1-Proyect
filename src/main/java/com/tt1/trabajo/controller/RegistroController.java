package com.tt1.trabajo.controller;

import com.tt1.trabajo.entity.UsuarioEntity;
import com.tt1.trabajo.repository.UsuarioRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistroController {

    private final UsuarioRepository userRepository;

    public RegistroController(UsuarioRepository UsRepo) {
        this.userRepository = UsRepo;
    }

    @GetMapping("/registro")
    public String showRegistro() {
        return "registro";
    }

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
