package servicios;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Clase de configuración para proveer instancias de registro (Loggers) a la aplicación.
 */
@Configuration
public class LoggerConfig {

    /**
     * Define un bean de Logger específico etiquetado como "Simulation" para trazar eventos.
     *
     * @return Instancia de {@link Logger}.
     */
    @Bean
    public Logger simulationLogger() {
        return LoggerFactory.getLogger("Simulation");
    }
}