package com.tt1.trabajo.controller;

import com.tt1.trabajo.entity.CorreoEntity;
import com.tt1.trabajo.repository.CorreoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/correos")
public class CorreoController {

    @Autowired
    private CorreoRepository correoRepository;

    @GetMapping("/recibidos")
    public String verRecibidos(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/";

        List<CorreoEntity> recibidos = correoRepository.findByDestinoOrderByFechaDesc(username);
        model.addAttribute("correos", recibidos);
        return "bandeja_entrada";
    }

    @GetMapping("/enviados")
    public String verEnviados(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/";

        List<CorreoEntity> enviados = correoRepository.findByOrigenOrderByFechaDesc(username);
        model.addAttribute("correos", enviados);
        return "correos_enviados";
    }

    @GetMapping("/nuevo")
    public String redactarCorreo(HttpSession session) {
        if (session.getAttribute("username") == null) return "redirect:/";
        return "enviar_correo";
    }

    @PostMapping("/enviar")
    public String procesarEnvio(@RequestParam String destino, @RequestParam String mensaje, HttpSession session) {
        String origen = (String) session.getAttribute("username");
        if (origen == null) return "redirect:/";

        String token = UUID.randomUUID().toString();
        CorreoEntity correo = new CorreoEntity(token, origen, destino, LocalDateTime.now(), mensaje);
        correoRepository.save(correo);

        return "redirect:/correos/enviados";
    }
}