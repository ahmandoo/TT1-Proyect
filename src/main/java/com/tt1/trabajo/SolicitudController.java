package com.tt1.trabajo;

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
 * Controlador encargado de manejar la visualización y el procesamiento 
 * de los formularios de solicitud de simulaciones.
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
     * Muestra la vista con el formulario para solicitar una nueva simulación.
     * * @param model   Modelo para pasar la lista de entidades a la vista.
     * @param session Sesión HTTP para verificar la autenticación.
     * @return El nombre de la vista "solicitud" o redirección a "/" si no hay sesión.
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
     * Procesa los datos enviados en el formulario de solicitud.
     * Realiza validaciones sobre las cantidades (no negativos, enteros) y la validez de las entidades.
     * * @param formData Mapa con los pares ID de Entidad - Cantidad recibidos del formulario.
     * @param model    Modelo para pasar errores o el token de resultado a la vista.
     * @param session  Sesión HTTP para obtener el usuario que realiza la petición.
     * @return La vista "formResult" indicando éxito o fallos.
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
