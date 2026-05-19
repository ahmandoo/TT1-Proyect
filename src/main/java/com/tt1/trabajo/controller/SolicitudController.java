package com.tt1.trabajo.controller;

import com.tt1.trabajo.entity.SolicitudEntity;
import com.tt1.trabajo.entity.UsuarioEntity;
import com.tt1.trabajo.repository.SolicitudRepository;
import com.tt1.trabajo.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import interfaces.InterfazContactoSim;
import interfaces.InterfazEnviarEmails; // Importación para el servicio de correos de Amudi
import modelo.DatosSolicitud;
import modelo.Destinatario;

/**
 * Controlador de Spring MVC que gestiona el ciclo de vida de una solicitud de simulación.
 * Maneja la presentación del formulario, la validación de los datos de entrada y
 * la comunicación con el servicio de simulación externo.
 */
@Controller
public class SolicitudController {

    private final InterfazContactoSim ics;
    private final Logger logger;
    private final UsuarioRepository usuarioRepo;
    private final SolicitudRepository solicitudRepo;
    private final InterfazEnviarEmails emailService; // Dependencia gestionada por Amudi

    /**
     * Constructor para inyectar las dependencias de servicios y repositorios.
     *
     * @param ics           Servicio para interactuar con la lógica de simulación externa.
     * @param logger        Componente para el registro de eventos, advertencias y errores.
     * @param usuarioRepo   Repositorio para la persistencia y consulta de usuarios.
     * @param solicitudRepo Repositorio para la persistencia y consulta de solicitudes.
     * @param emailService  Interfaz de envío de correos electrónicos.
     */
    public SolicitudController(InterfazContactoSim ics, Logger logger,
                               UsuarioRepository usuarioRepo, SolicitudRepository solicitudRepo,
                               InterfazEnviarEmails emailService) {
        this.ics = ics;
        this.logger = logger;
        this.usuarioRepo = usuarioRepo;
        this.solicitudRepo = solicitudRepo;
        this.emailService = emailService;
    }

    /**
     * Prepara y muestra el formulario de solicitud de simulaciones.
     * Verifica que el usuario tenga una sesión activa; de lo contrario, redirige al inicio.
     * Carga las entidades disponibles para la simulación mediante {@link InterfazContactoSim}.
     *
     * @param model   Modelo de Spring para añadir los atributos de entidades a la vista.
     * @param session Sesión actual del usuario para validar la autenticación.
     * @return String con el nombre de la plantilla HTML ("solicitud") o redirección si no hay sesión.
     */
    @GetMapping("/solicitud")
    public String solicitud(Model model, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/";
        }
        model.addAttribute("entities", ics.getEntities());
        return "solicitud";
    }

    /**
     * Procesa el envío del formulario, valida los datos de la pandemia e inicia una nueva simulación.
     * <p>
     * El proceso incluye:
     * <ul>
     * <li>Recogida de parámetros de simulación personalizados (dimensiones, población inicial y movilidad).</li>
     * <li>Almacenamiento de dichos parámetros en la sesión HTTP para su posterior lectura en la cuadrícula.</li>
     * <li>Persistencia de la solicitud con estado "PENDIENTE" si el servidor remoto acepta la petición.</li>
     * <li>Envío opcional de una notificación por correo electrónico delegada al servicio correspondiente.</li>
     * </ul>
     *
     * @param ancho              Dimensión de la cuadrícula o mapa de simulación.
     * @param generaciones       Duración total de la simulación expresada en segundos.
     * @param infectadosInit     Número inicial de celdas infectadas (Pacientes cero).
     * @param vacunadosInit      Número inicial de celdas con inmunidad por vacunación.
     * @param porcentajeViajeros Proporción de la población con alta tasa de movilidad intergrupal.
     * @param enviarCorreo       Estado del checkbox opcional para requerir aviso por email ("true" o null).
     * @param model              Modelo de Spring para devolver la lista de errores o el token generado a la vista.
     * @param session            Sesión HTTP para identificar al autor de la solicitud y persistir parámetros del entorno.
     * @return El nombre de la vista HTML ("formResult") con el resumen y resultado de la operación.
     */
    @PostMapping("/solicitud")
    public String handleSolicitud(
            @RequestParam int ancho,
            @RequestParam int generaciones,
            @RequestParam int infectadosInit,
            @RequestParam int vacunadosInit,
            @RequestParam int porcentajeViajeros,
            @RequestParam(required = false) String enviarCorreo,
            Model model, HttpSession session) {

        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/";
        }

        logger.info("Atendida petición de simulación personalizada para el usuario: " + username);

        // [TAREA DE MARIO] Almacenamiento dinámico de parámetros en sesión para el GridController
        session.setAttribute("sim_ancho", ancho);
        session.setAttribute("sim_generaciones", generaciones);
        session.setAttribute("sim_infectados", infectadosInit);
        session.setAttribute("sim_vacunados", vacunadosInit);
        session.setAttribute("sim_movilidad", porcentajeViajeros);

        // Generación del payload para mantener compatibilidad con el endpoint externo de simulación
        Map<Integer, Integer> dummyData = new HashMap<>();
        dummyData.put(1, 1);
        DatosSolicitud ds = new DatosSolicitud(dummyData);

        int tok = ics.solicitarSimulation(ds, username);

        if(tok != -1) {
            UsuarioEntity user = usuarioRepo.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado en la base de datos"));

            solicitudRepo.save(new SolicitudEntity(tok, user, "PENDIENTE"));
            model.addAttribute("token", tok);

            // =========================================================================
            // [TAREA DE AMUDI] - BLOQUE DE ENVÍO DE CORREO ELECTRÓNICO ELECTIVO
            // =========================================================================
            if ("true".equals(enviarCorreo) && user.getEmail() != null && !user.getEmail().isEmpty()) {

                String cuerpoMensaje = "ASUNTO: Simulación Epidemiológica Registrada\n\n"
                        + "Hola " + username + ",\n\n"
                        + "Tu simulación con parámetros personalizados se ha procesado con éxito.\n"
                        + "Puedes visualizar la evolución del grid con tu token privado: " + tok + "\n\n"
                        + "Atentamente,\nEl Equipo de Desarrollo TT1.";

                Destinatario destinatario = new Destinatario(user.getEmail());
                emailService.enviarEmail(destinatario, cuerpoMensaje);

                logger.info("Notificación por correo electrónico enviada con éxito a: " + user.getEmail());
            }
            // =========================================================================

        } else {
            logger.error("Error en comunicación con el servidor remoto de simulación");
        }

        return "formResult";
    }

    /**
     * Recupera y muestra el historial de simulaciones solicitadas por el usuario actual.
     *
     * @param model   Modelo de Spring para pasar la lista de {@link SolicitudEntity} a la vista.
     * @param session Sesión HTTP actual para filtrar las solicitudes por nombre de usuario.
     * @return El nombre de la plantilla HTML ("historial") o redirección a la raíz si no hay sesión.
     */
    @GetMapping("/historial")
    public String mostrarHistorial(Model model, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) return "redirect:/";
        List<SolicitudEntity> misSolicitudes = solicitudRepo.findByUsuarioUsername(username);
        model.addAttribute("solicitudes", misSolicitudes);
        return "historial";
    }
}