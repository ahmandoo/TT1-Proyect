package servicios;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import utilidades.ApiClient;
import utilidades.api.EmailApi;
import utilidades.api.SolicitudApi;
import utilidades.api.ResultadosApi;

/**
 * Configuración de Spring para instanciar e inyectar los clientes de las distintas APIs externas.
 */
@Configuration
public class ApiClientConfig {

    /**
     * Configura el cliente HTTP base indicando el host, puerto y esquema de conexión.
     *
     * @return Instancia configurada de {@link ApiClient}.
     */
    @Bean
    public ApiClient apiClient() {
        ApiClient apiClient = new ApiClient();
        apiClient.setHost("localhost");
        apiClient.setPort(8080);
        apiClient.setScheme("http");
        return apiClient;
    }

    /**
     * Crea el bean para consumir los endpoints relacionados con las solicitudes.
     *
     * @param apiClient Cliente HTTP base inyectado.
     * @return Instancia de {@link SolicitudApi}.
     */
    @Bean
    public utilidades.api.SolicitudApi solicitudApi(ApiClient apiClient) {
        return new utilidades.api.SolicitudApi(apiClient);
    }

    /**
     * Crea el bean para consumir los endpoints relacionados con la descarga de resultados.
     *
     * @param apiClient Cliente HTTP base inyectado.
     * @return Instancia de {@link ResultadosApi}.
     */
    @Bean
    public utilidades.api.ResultadosApi resultadosApi(ApiClient apiClient) {
        return new utilidades.api.ResultadosApi(apiClient);
    }

    /**
     * Crea el bean para consumir los endpoints relacionados con el envío de correos electrónicos.
     *
     * @param apiClient Cliente HTTP base inyectado.
     * @return Instancia de {@link EmailApi}.
     */
    @Bean
    public EmailApi emailApi(ApiClient apiClient) {
        return new EmailApi(apiClient);
    }
}
