package modelo;

import java.util.List;
import java.util.Map;

/**
 * Clase que encapsula los resultados completos de una simulación descargada desde la API externa.
 * Contiene la información necesaria para renderizar o analizar el histórico de estados del tablero.
 */
public class DatosSimulation {

	/**
	 * Constructor por defecto.
	 */
	public DatosSimulation() {}

	/**
	 * Duración total de la simulación expresada en segundos o generaciones.
	 */
	private int maxSegundos;

	/**
	 * Dimensión lateral del tablero asumiendo que es cuadrado.
	 */
	private int anchoTablero;

	/**
	 * Mapa que asocia cada instante de tiempo con la lista de puntos
	 * o celdas y sus respectivos colores en ese momento.
	 */
	private Map<Integer,List<Punto>> puntos;

	/**
	 * Obtiene el número máximo de segundos o generaciones de la simulación.
	 * @return El número máximo de segundos.
	 */
	public int getMaxSegundos() {
		return maxSegundos;
	}

	/**
	 * Establece el número máximo de segundos o generaciones de la simulación.
	 * @param s El número máximo de segundos.
	 */
	public void setMaxSegundos(int s) {
		this.maxSegundos = s;
	}

	/**
	 * Obtiene el ancho del tablero de la simulación.
	 * @return El tamaño del lado del tablero.
	 */
	public int getAnchoTablero() {
		return anchoTablero;
	}

	/**
	 * Establece el ancho del tablero de la simulación.
	 * @param s El tamaño del lado del tablero.
	 */
	public void setAnchoTablero(int s) {
		this.anchoTablero = s;
	}

	/**
	 * Obtiene el mapa con el historial de puntos de la simulación.
	 * @return Un mapa donde la clave es el tiempo y el valor es la lista de puntos en ese instante.
	 */
	public Map<Integer,List<Punto>> getPuntos(){
		return puntos;
	}

	/**
	 * Establece el mapa con el historial de puntos de la simulación.
	 * @param s El mapa de instantes de tiempo y sus correspondientes listas de puntos.
	 */
	public void setPuntos(Map<Integer,List<Punto>> s) {
		this.puntos = s;
	}
}