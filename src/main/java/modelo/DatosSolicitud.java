package modelo;

import java.util.Map;

/**
 * Clase que representa los parámetros de configuración necesarios para solicitar
 * la ejecución de una nueva simulación de epidemia en el sistema.
 */
public class DatosSolicitud {

	/**
	 * Mapa que asocia el identificador de una entidad adicional con la cantidad solicitada.
	 */
	private Map<Integer, Integer> nums;

	/**
	 * Tamaño del ancho del tablero de la simulación.
	 */
	private int ancho;

	/**
	 * Duración total de la simulación en ciclos o generaciones.
	 */
	private int generaciones;

	/**
	 * Número inicial de individuos infectados al comienzo de la simulación.
	 */
	private int infectadosInit;

	/**
	 * Número inicial de individuos con inmunidad al comienzo de la simulación.
	 */
	private int vacunadosInit;

	/**
	 * Porcentaje de la población de 0 a 100 que exhibirá un comportamiento de alta movilidad.
	 */
	private int porcentajeViajeros;

	/**
	 * Constructor básico que inicializa únicamente el mapa de cantidades de entidades.
	 * @param nums Mapa con los IDs de las entidades y sus cantidades.
	 */
	public DatosSolicitud(Map<Integer, Integer> nums) {
		this.nums = nums;
	}

	/**
	 * Constructor completo para inicializar todos los parámetros de configuración de la pandemia.
	 *
	 * @param nums               Mapa con los IDs de las entidades y sus cantidades.
	 * @param ancho              Dimensión lateral del mapa.
	 * @param generaciones       Total de iteraciones temporales de la simulación.
	 * @param infectadosInit     Cantidad inicial de pacientes infectados.
	 * @param vacunadosInit      Cantidad inicial de población vacunada.
	 * @param porcentajeViajeros Proporción de la población que se desplaza frecuentemente.
	 */
	public DatosSolicitud(Map<Integer, Integer> nums, int ancho, int generaciones, int infectadosInit, int vacunadosInit, int porcentajeViajeros) {
		this.nums = nums;
		this.ancho = ancho;
		this.generaciones = generaciones;
		this.infectadosInit = infectadosInit;
		this.vacunadosInit = vacunadosInit;
		this.porcentajeViajeros = porcentajeViajeros;
	}

	/**
	 * @return El mapa con las cantidades de entidades base asociadas por su ID.
	 */
	public Map<Integer, Integer> getNums() {
		return nums;
	}

	/**
	 * @return La dimensión configurada para el ancho del tablero.
	 */
	public int getAncho() {
		return ancho;
	}

	/**
	 * @return El número total de generaciones o ciclos de tiempo a simular.
	 */
	public int getGeneraciones() {
		return generaciones;
	}

	/**
	 * @return La cantidad de población infectada en el inicio de la simulación.
	 */
	public int getInfectadosInit() {
		return infectadosInit;
	}

	/**
	 * @return La cantidad de población vacunada en el inicio de la simulación.
	 */
	public int getVacunadosInit() {
		return vacunadosInit;
	}

	/**
	 * @return El porcentaje de la población designado como viajeros de alta movilidad.
	 */
	public int getPorcentajeViajeros() {
		return porcentajeViajeros;
	}
}