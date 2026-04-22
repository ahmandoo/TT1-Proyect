package com.tt1.trabajo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SOLICITUDES")
public class SolicitudEntity {
    @Id
    private Integer token;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuario;

    private LocalDateTime fechaPeticion;
    private String estado;

    public SolicitudEntity() {}

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