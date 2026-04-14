package servicios;

import interfaces.InterfazEnviarEmails;
import modelo.Destinatario;
import org.springframework.stereotype.Service;
import utilidades.api.EmailApi;
/**
 * Servicio encargado del envío de correos utilizando la configuración de EmailApi.
 */
@Service
public class EnviarEmails implements InterfazEnviarEmails {
    private final EmailApi emailApi;

    public EnviarEmails(EmailApi emailApi) {
        this.emailApi = emailApi;
    }

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
