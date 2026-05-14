package servicios;

import interfaces.InterfazEnviarEmails;
import modelo.Destinatario;
import org.springframework.stereotype.Service;
import utilidades.api.EmailApi;
/**
 * Servicio encargado de gestionar el envío de correos electrónicos haciendo uso
 * del cliente generado EmailApi.
 */
@Service
public class EnviarEmails implements InterfazEnviarEmails {
    private final EmailApi emailApi;
    /**
     * Constructor para inyectar el cliente de la API de correos.
     *
     * @param emailApi Cliente generado por OpenAPI para interactuar con el endpoint de envío de emails.
     */
    public EnviarEmails(EmailApi emailApi) {
        this.emailApi = emailApi;
    }
    /**
     * Envía un correo electrónico haciendo una llamada POST a la API externa.
     * Actualmente está configurado para enviar por defecto al correo "alumno@unirioja.es".
     *
     * @param dest   Objeto que representa al destinatario del correo (no usado de forma activa en la implementación actual).
     * @param mensaje Contenido en formato texto del mensaje a enviar.
     * @return {@code true} si la petición a la API se realizó exitosamente; {@code false} en caso de que ocurra una excepción.
     */
    @Override
    public boolean enviarEmail(Destinatario dest, String mensaje) {
        try {
            emailApi.emailPost("alumno@unirioja.es", mensaje);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
