package com.tt1.trabajo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Clase principal que arranca la aplicación Spring Boot.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"servicios", "com.tt1.trabajo", "interfaces"})
public class TrabajoApplication {
	/**
	 * Método principal que inicia el contexto de Spring.
	 *
	 * @param args Argumentos de línea de comandos.
	 */
	public static void main(String[] args) {
		SpringApplication.run(TrabajoApplication.class, args);
	}

}
