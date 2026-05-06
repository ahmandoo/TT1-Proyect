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

/**
 * Controlador de Spring MVC encargado de gestionar el flujo de correos.
 * Permite a los usuarios ver su bandeja de entrada, correos enviados y enviar nuevos mensajes.
 */
@Controller
public class CorreoController {

    @Autowired
    private CorreoRepository correoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private InterfazContactoSim ics;

    /**
     * Muestra la bandeja de entrada del usuario con los correos recibidos ordenados por fecha.
     *
     * @param session Sesión HTTP actual para verificar si el usuario está autenticado.
     * @param model   Modelo de Spring para pasar la lista de correos a la vista.
     * @return El nombre de la vista HTML de la bandeja de entrada, o redirección al inicio si no hay sesión.
     */
    @GetMapping("/correos/bandeja")
    public String verBandejaEntrada(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/";
        List<CorreoEntity> recibidos = correoRepository.findByDestinoOrderByFechaDesc(username);

        model.addAttribute("correos", recibidos);
        model.addAttribute("usuarioActual", username);
        return "solicitud";
    }

    /**
     * Muestra la vista con el historial de correos enviados por el usuario actual.
     *
     * @param session Sesión HTTP actual para obtener el usuario autenticado.
     * @param model   Modelo de Spring para pasar la lista de correos enviados a la vista.
     * @return El nombre de la vista HTML "correos_enviados", o redirección al inicio si no hay sesión.
     */
    @GetMapping("/correos/enviados")
    public String verEnviados(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/";
        List<CorreoEntity> enviados = correoRepository.findByOrigenOrderByFechaDesc(username);

        model.addAttribute("correos", enviados);
        model.addAttribute("usuarioActual", username);
        return "correos_enviados";
    }

    /**
     * Muestra el formulario para redactar un nuevo correo.
     *
     * @param session Sesión HTTP actual.
     * @param model   Modelo de Spring para pasar el nombre del usuario actual a la vista.
     * @return El nombre de la vista HTML "enviar_correo", o redirección al inicio si no hay sesión.
     */
    @GetMapping("/correos/nuevo")
    public String redactarCorreo(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/";

        model.addAttribute("usuarioActual", username);
        return "enviar_correo";
    }

    /**
     * Procesa el envío de un nuevo correo desde el formulario.
     * Verifica que el destinatario exista, solicita una nueva simulación asociada y guarda el correo.
     *
     * @param destino Nombre del usuario que recibirá el correo.
     * @param mensaje Contenido del mensaje a enviar.
     * @param session Sesión HTTP para identificar al remitente.
     * @param model   Modelo de Spring para devolver mensajes de error si el destinatario no existe.
     * @return Redirección a la vista de correos enviados si tiene éxito, o recarga el formulario en caso de error.
     */
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