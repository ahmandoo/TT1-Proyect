package com.tt1.trabajo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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

    public CorreoEntity() {}

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