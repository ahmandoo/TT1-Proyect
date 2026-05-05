package com.tt1.trabajo;

import com.tt1.trabajo.entity.CorreoEntity;
import com.tt1.trabajo.repository.CorreoRepository;
import com.tt1.trabajo.repository.UsuarioRepository;
import interfaces.InterfazContactoSim;
import modelo.DatosSolicitud;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class CorreoController {

    @Autowired
    private CorreoRepository correoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private InterfazContactoSim ics;

    @GetMapping("/correos/bandeja")
    public String verBandejaEntrada(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/";
        List<CorreoEntity> recibidos = correoRepository.findByDestinoOrderByFechaDesc(username);

        model.addAttribute("correos", recibidos);
        model.addAttribute("usuarioActual", username);
        return "solicitud";
    }

    @GetMapping("/correos/enviados")
    public String verEnviados(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/";
        List<CorreoEntity> enviados = correoRepository.findByOrigenOrderByFechaDesc(username);

        model.addAttribute("correos", enviados);
        model.addAttribute("usuarioActual", username);
        return "correos_enviados";
    }

    @GetMapping("/correos/nuevo")
    public String redactarCorreo(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/";

        model.addAttribute("usuarioActual", username);
        return "enviar_correo";
    }

    @PostMapping("/correos/enviar")
    public String procesarEnvio(@RequestParam String destino, @RequestParam String mensaje, HttpSession session, Model model) {
        String origen = (String) session.getAttribute("username");
        if (origen == null) return "redirect:/";

        if (!usuarioRepository.existsByUsername(destino)) {
            model.addAttribute("error", "El usuario de destino no existe en el sistema.");
            model.addAttribute("usuarioActual", origen);
            return "enviar_correo";
        }

        Map<Integer, Integer> validData = new HashMap<>();
        DatosSolicitud ds = new DatosSolicitud(validData);
        int tok = ics.solicitarSimulation(ds, origen);

        CorreoEntity correo = new CorreoEntity(String.valueOf(tok), origen, destino, LocalDateTime.now(), mensaje);
        correoRepository.save(correo);

        return "redirect:/correos/enviados";
    }
}