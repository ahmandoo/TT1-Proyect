package com.tt1.trabajo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa una solicitud de simulación en el sistema.
 * Mapeada a la tabla "SOLICITUDES".
 */
@Entity
@Table(name = "SOLICITUDES")
public class SolicitudEntity {

    /**
     * Identificador único de la solicitud (Token).
     */
    @Id
    private Integer token;

    /**
     * Usuario que ha realizado la solicitud.
     * Relación de muchos a uno con {@link UsuarioEntity}.
     */
    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuario;

    /**
     * Fecha y hora en la que se registró la petición.
     */
    private LocalDateTime fechaPeticion;

    /**
     * Estado actual de la simulación (ej. "PENDIENTE", "PROCESADO").
     */
    private String estado;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public SolicitudEntity() {}

    /**
     * Constructor para crear una nueva solicitud con fecha automática.
     * @param token Identificador de la petición.
     * @param usuario Entidad del usuario solicitante.
     * @param estado Estado inicial de la petición.
     */
    public SolicitudEntity(Integer token, UsuarioEntity usuario, String estado) {
        this.token = token;
        this.usuario = usuario;
        this.estado = estado;
        this.fechaPeticion = LocalDateTime.now();
    }

    // Getters y Setters
    public Integer getToken() {
        return token;
    }

    public void setToken(Integer token) {
        this.token = token;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFechaPeticion() {
        return fechaPeticion;
    }

    public void setFechaPeticion(LocalDateTime fechaPeticion) {
        this.fechaPeticion = fechaPeticion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}