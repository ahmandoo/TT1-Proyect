package com.tt1.trabajo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa un correo enviado o recibido en el sistema.
 * Mapeada a la tabla "CORREOS" en la base de datos.
 */
@Entity
@Table(name = "CORREOS")
public class CorreoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "correo_seq")
    @SequenceGenerator(name = "correo_seq", sequenceName = "CORREO_SEQ", allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private String origen;

    @Column(nullable = false)
    private String destino;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Lob
    @Column(nullable = false)
    private String mensaje;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public CorreoEntity() {}

    /**
     * Constructor con todos los campos necesarios para crear un nuevo correo.
     *
     * @param token   Identificador de la simulación asociada.
     * @param origen  Nombre del usuario que envía el correo.
     * @param destino Nombre del usuario que recibe el correo.
     * @param fecha   Fecha y hora de envío.
     * @param mensaje Contenido del correo.
     */
    public CorreoEntity(String token, String origen, String destino, LocalDateTime fecha, String mensaje) {
        this.token = token;
        this.origen = origen;
        this.destino = destino;
        this.fecha = fecha;
        this.mensaje = mensaje;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}