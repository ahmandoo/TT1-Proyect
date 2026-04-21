package com.tt1.trabajo.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "USUARIOS")
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<SolicitudEntity> solicitudes;

    public UsuarioEntity() {}
    public UsuarioEntity(String username) { this.username = username; }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
