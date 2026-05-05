package com.tt1.trabajo;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import interfaces.InterfazContactoSim;
import modelo.DatosSolicitud;
/**
 * Controlador de Spring MVC que gestiona el ciclo de vida de una solicitud de simulación.
 * Maneja la presentación del formulario, la validación de datos de entrada y
 * la comunicación con el servicio de simulación.
 */
@Controller
public class SolicitudController {
	
	private final InterfazContactoSim ics;
	private final Logger logger;
    private final UsuarioRepository usuarioRepo;
    private final SolicitudRepository solicitudRepo;
	/**
     * Constructor para inyectar dependencias de servicios y repositorios.
     * @param ics           Servicio para interactuar con la lógica de simulación externa.
     * @param logger        Componente para el registro de eventos y errores.
     * @param usuarioRepo   Repositorio para la persistencia de usuarios.
     * @param solicitudRepo Repositorio para la persistencia de solicitudes.
     */
    public SolicitudController(InterfazContactoSim ics, Logger logger,
                               UsuarioRepository usuarioRepo, SolicitudRepository solicitudRepo) {
        this.ics = ics;
        this.logger = logger;
        this.usuarioRepo = usuarioRepo;
        this.solicitudRepo = solicitudRepo;
    }

    /**
     * Prepara y muestra el formulario de solicitud.
     * Verifica que el usuario tenga una sesión activa; de lo contrario, redirige al inicio.
     * Carga las entidades disponibles para la simulación mediante {@link InterfazContactoSim}.
     *  @param model Objeto para añadir atributos a la vista.
     * @param session Sesión actual del usuario para validar autenticación.
     * @return String con el nombre de la plantilla HTML ("solicitud") o redirección.
     */
    /*
    @GetMapping("/solicitud")
    public String solicitud(Model model, HttpSession session) {
        if (session.getAttribute("username") == null) {
            return "redirect:/";
        }
        model.addAttribute("entities", ics.getEntities());
        return "solicitud";
    }*/
	
   /**
     * Procesa el envío del formulario, valida los datos e inicia la simulación.
     * <p>
     * El proceso incluye:
     * <ul>
     * <li>Validación de sesión y formato numérico de los datos.</li>
     * <li>Comprobación de IDs de entidad válidos mediante {@link InterfazContactoSim}.</li>
     * <li>Persistencia de la solicitud con estado "PENDIENTE" si el servidor acepta la petición.</li>
     * </ul>
     * @param formData Mapa con los datos del formulario (ID de entidad y cantidad).
     * @param model    Objeto para devolver errores o el token generado a la vista.
     * @param session  Sesión para identificar al autor de la solicitud.
     * @return La vista "formResult" con el resumen de la operación.
     */
    @PostMapping("/solicitud")
    public String handleSolicitud(@RequestParam Map<String, String> formData, Model model, HttpSession session) {

        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/";
        }

    	Map<Integer, Integer> validData = new HashMap<>();
        List<String> errors = new ArrayList<>();

        formData.forEach((key, value) -> {
            try {
                int num = Integer.parseInt(value);
                if (num < 0) {
                    errors.add(key + " no puede ser negativo");
                }
                int id = Integer.parseInt(key);
                if (ics.isValidEntityId(id)) {
                	validData.put(id, num);
                } else {
                	errors.add(key + "no se corresponde con una entidad");
                }
            } catch (NumberFormatException e) {
                errors.add(key + " debe ser un número entero");
            }
        });
        if(!errors.isEmpty()) {
        	model.addAttribute("errors", errors);
        	logger.warn("Atendida petición con errores");
        } else {
        	logger.info("Atendida petición");
        	DatosSolicitud ds = new DatosSolicitud(validData);
        	int tok = ics.solicitarSimulation(ds, username);
            if(tok != -1) {
                UsuarioEntity user = usuarioRepo.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                solicitudRepo.save(new SolicitudEntity(tok, user, "PENDIENTE"));
                model.addAttribute("token", tok);
            }
            else {
        		logger.error("Error en comunicación con servidor de simulación");
        	}
        }
        return "formResult";
    }

	/**
     * Recupera y muestra el historial de simulaciones solicitadas por el usuario actual.
     * @param model   Objeto para pasar la lista de {@link SolicitudEntity} a la vista.
     * @param session Sesión actual para filtrar las solicitudes por nombre de usuario.
     * @return El nombre de la plantilla "historial" o redirección a la raíz si no hay sesión.
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
