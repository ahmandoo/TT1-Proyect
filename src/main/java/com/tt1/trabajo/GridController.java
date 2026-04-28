package com.tt1.trabajo;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;

import interfaces.InterfazContactoSim;

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
import modelo.DatosSimulation;
import modelo.DatosSolicitud;
import modelo.Punto;
/**
 * Controlador encargado de gestionar y renderizar la vista de la cuadrícula (grid) 
 * correspondiente a los resultados de una simulación.
 */
@Controller
public class GridController {
	private final InterfazContactoSim ics;
	private final Logger logger;
	/**
     * Constructor para inyectar las dependencias necesarias.
     * * @param ics    Servicio de interfaz para comunicarse con el sistema de simulación.
     * @param logger Componente para el registro de trazas y eventos.
     */
	public GridController(InterfazContactoSim ics, Logger logger) {
		this.ics = ics;
		this.logger = logger;
	}
	/**
     * Maneja la petición GET para visualizar la cuadrícula de simulación.
     * Extrae los datos de la simulación usando el token proporcionado y prepara el mapa de colores.
     * * @param tok     Token o identificador único de la simulación descargada.
     * @param model   Modelo de Spring para pasar los atributos a la plantilla de Thymeleaf.
     * @param session Sesión HTTP actual para verificar si el usuario ha iniciado sesión.
     * @return El nombre de la vista HTML ("grid") o una redirección a la raíz si no hay sesión.
     */
	@GetMapping("/grid")
	public String mostrarGrid(@RequestParam("tok") int tok, Model model, HttpSession session) {
		String username = (String) session.getAttribute("username");
		if (username == null) {
			return "redirect:/";
		}
		DatosSimulation ds = ics.descargarDatos(tok, username);
		model.addAttribute("ancho", ds.getAnchoTablero());
		model.addAttribute("maxT", ds.getMaxSegundos());
		Map<String, String> colors = new HashMap<>();
		for(int t = 0; t < ds.getMaxSegundos(); t++) {
			List<Punto> puntosEnT = ds.getPuntos().get(t);
			if (puntosEnT != null) {
				for(Punto p : puntosEnT) {
					colors.put(t + "-" + p.getY() + "-" + p.getX(), p.getColor());
				}
			}
		}
		model.addAttribute("colors", colors);
		return "grid";
	}
}
