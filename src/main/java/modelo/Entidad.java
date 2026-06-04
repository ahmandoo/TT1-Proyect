package modelo;

/**
 * Clase que representa un tipo de entidad base disponible en el sistema externo de simulaciones
 * por ejemplo Viviendas, Coches, Temperatura o Combustible.
 */
public class Entidad {

	/**
	 * Identificador numérico único del tipo de entidad.
	 */
	private int id;

	/**
	 * Nombre descriptivo de la entidad.
	 */
	private String name;

	/**
	 * Descripción detallada sobre la función o naturaleza de la entidad.
	 */
	private String descripcion;

	/**
	 * Obtiene el identificador numérico de la entidad.
	 * @return El ID de la entidad.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Establece el identificador numérico de la entidad.
	 * @param id El nuevo ID a asignar.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Obtiene el nombre de la entidad.
	 * @return El nombre de la entidad.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Establece el nombre de la entidad.
	 * @param name El nuevo nombre a asignar.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Obtiene la descripción detallada de la entidad.
	 * @return La descripción de la entidad.
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Establece la descripción detallada de la entidad.
	 * @param descripcion La nueva descripción a asignar.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}