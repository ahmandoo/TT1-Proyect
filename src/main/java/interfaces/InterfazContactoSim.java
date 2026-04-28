package interfaces;

import java.util.List;

import modelo.DatosSimulation;
import modelo.DatosSolicitud;
import modelo.Entidad;
/**
 * Interfaz que define las operaciones disponibles para comunicarse 
 * con el sistema externo de simulaciones mediante API.
 */
public interface InterfazContactoSim {
	/**
     * Envía una petición para iniciar una nueva simulación.
     * * @param sol     Objeto con los datos numéricos de la solicitud.
     * @param usuario Nombre del usuario que realiza la solicitud.
     * @return Un token numérico que identifica la simulación, o -1 en caso de error.
     */
	public int solicitarSimulation(DatosSolicitud sol, String usuario);
	/**
     * Descarga y parsea los datos estructurados de una simulación finalizada.
     * * @param ticket Token identificador de la solicitud previa.
     * @return Un objeto DatosSimulation con el estado, los tiempos y puntos del tablero.
     */
	public DatosSimulation descargarDatos(int ticket,String usuario);
	/**
     * Obtiene la lista completa de entidades disponibles en el sistema.
     * * @return Lista de objetos Entidad monitorizables.
     */
	public List<Entidad> getEntities();
	/**
     * Verifica si un identificador corresponde a una entidad válida y existente.
     * * @param id El identificador numérico a verificar.
     * @return true si la entidad existe, false de lo contrario.
     */
	public boolean isValidEntityId(int id);
}
