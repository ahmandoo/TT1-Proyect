package com.tt1.trabajo;

import com.tt1.trabajo.repository.SolicitudRepository;
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
	/**
     * Constructor para inyectar dependencias.
     * * @param ics    Servicio para interactuar con la lógica de simulación y las entidades.
     * @param logger Logger para registrar eventos.
     */
	public SolicitudController(InterfazContactoSim ics, Logger logger) {
		this.ics = ics;
		this.logger = logger;
	}

    /**
     * Prepara y muestra el formulario de solicitud.
     * Verifica que el usuario tenga una sesión activa; de lo contrario, redirige al inicio.
     * Carga las entidades disponibles para la simulación mediante {@link InterfazContactoSim}.
     * * @param model Objeto para añadir atributos a la vista.
     * @param session Sesión actual del usuario para validar autenticación.
     * @return String con el nombre de la plantilla HTML ("solicitud") o redirección.
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
     * Procesa el envío del formulario de solicitud.
     * Realiza las siguientes validaciones:
     * 1. Existencia de sesión de usuario.
     * 2. Conversión de cantidades a enteros.
     * 3. Comprobación de que las cantidades no sean negativas.
     * 4. Validación de IDs de entidad existentes.
     * * Si hay errores, los añade al modelo para mostrarlos en "formResult".
     * Si los datos son válidos, inicia la simulación y genera un token.
     * * @param formData Mapa con los datos del formulario (ID Entidad -> Cantidad).
     * @param model Objeto para pasar errores o el token resultante a la vista.
     * @param session Sesión para identificar al autor de la solicitud.
     * @return La vista "formResult" con el resultado de la operación.
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
        		model.addAttribute("token", tok);
        	} else {
        		logger.error("Error en comunicación con servidor de simulación");
        	}
        }
        return "formResult";
    }

}
