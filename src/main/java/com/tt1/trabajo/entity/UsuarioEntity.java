package com.tt1.trabajo.entity;

import jakarta.persistence.*;
import java.util.List;

/**
 * Entidad que representa a un usuario registrado en el sistema.
 * Mapeada a la tabla "USUARIOS".
 */
@Entity
@Table(name = "USUARIOS")
public class UsuarioEntity {

    /**
     * Identificador único autoincremental del usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de usuario único para el acceso al sistema.
     */
    @Column(unique = true, nullable = false)
    private String username;

    /**
     * Lista de solicitudes asociadas a este usuario.
     * Se gestiona en cascada para reflejar cambios en las solicitudes vinculadas.
     */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<SolicitudEntity> solicitudes;

    /**
     * Constructor por defecto.
     */
    public UsuarioEntity() {}

    /**
     * Constructor para inicializar un usuario por su nombre.
     * @param username Nombre único del usuario.
     */
    public UsuarioEntity(String username) { this.username = username; }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
