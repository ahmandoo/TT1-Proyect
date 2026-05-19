package com.tt1.trabajo.controller;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;

import interfaces.InterfazContactoSim;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import modelo.DatosSimulation;
import servicios.GameService;

/**
 * Controlador encargado de gestionar y renderizar la vista de la cuadrícula (grid) 
 * correspondiente a los resultados de una simulación.
 */
@Controller
public class GridController {
	private final InterfazContactoSim ics;
	private final Logger logger;

	private final GameService gameService;

	/**
	 * Constructor para inyectar las dependencias necesarias.
	 * * @param ics         Servicio de interfaz para comunicarse con el sistema de simulación.
	 * @param logger      Componente para el registro de trazas y eventos.
	 * @param gameService Servicio que gestiona la lógica principal de la simulación del juego.
	 */

	public GridController(InterfazContactoSim ics, Logger logger, GameService gameService) {
		this.ics = ics;
		this.logger = logger;
		this.gameService = gameService;
	}

	/**
	 * Maneja la petición GET para visualizar la cuadrícula de simulación.
	 * Extrae los datos de la simulación usando el token proporcionado, procesa la
	 * simulación completa y prepara el mapa de colores para la vista.
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

		Integer anchoSesion = (Integer) session.getAttribute("sim_ancho");
		Integer generacionesSesion = (Integer) session.getAttribute("sim_generaciones");
		Integer infectadosSesion = (Integer) session.getAttribute("sim_infectados");
		Integer vacunadosSesion = (Integer) session.getAttribute("sim_vacunados");
		Integer movilidadSesion = (Integer) session.getAttribute("sim_movilidad");

		int ancho = (anchoSesion != null) ? anchoSesion : 50;
		int maxSegundos = (generacionesSesion != null) ? generacionesSesion : 80;
		int infectadosInit = (infectadosSesion != null) ? infectadosSesion : 5;
		int vacunadosInit = (vacunadosSesion != null) ? vacunadosSesion : 10;
		int porcentajeViajeros = (movilidadSesion != null) ? movilidadSesion : 20;

		double porcentajeInfeccion = 0.50;

		model.addAttribute("count", ancho);
		model.addAttribute("maxTime", maxSegundos);

		int[][] gridLimpio = new int[ancho][ancho];
		for(int i = 0; i < ancho; i++) {
			for(int j = 0; j < ancho; j++) {
				gridLimpio[i][j] = 1;
			}
		}

		gameService.configurarSimulacion(infectadosInit, vacunadosInit, porcentajeViajeros, porcentajeInfeccion);
		List<int[][]> historialJuego = gameService.procesarSimulacionCompleta(gridLimpio, maxSegundos);
		Map<String, String> colors = new HashMap<>();

		for(int t = 0; t < historialJuego.size(); t++) {
			int[][] gridActual = historialJuego.get(t);

			for(int y = 0; y < ancho; y++) {
				for(int x = 0; x < ancho; x++) {
					int estado = gridActual[y][x];
					String colorHex = "";

					if (estado == -1) colorHex = "white";
					else if (estado == 1) colorHex = "blue";
					else if (estado == 0) colorHex = "red";
					else if (estado == 2) colorHex = "black";
					else if (estado == 3) colorHex = "green";
					else if (estado == 4) colorHex = "yellow";

					colors.put(t + "-" + y + "-" + x, colorHex);
				}
			}
		}

		model.addAttribute("colors", colors);
		return "grid";
	}
}
