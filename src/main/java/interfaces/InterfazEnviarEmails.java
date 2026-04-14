package interfaces;

import modelo.Destinatario;
/**
 * Interfaz que define el contrato para el servicio de envío de notificaciones por correo.
 */
public interface InterfazEnviarEmails {
	/**
     * Envía un mensaje de correo a un destinatario específico.
     * * @param dest  Objeto que representa al destinatario del correo.
     * @param email Contenido en texto del mensaje a enviar.
     * @return true si el correo se envió con éxito, false en caso de fallo.
     */
	public boolean enviarEmail(Destinatario dest, String email);
}
