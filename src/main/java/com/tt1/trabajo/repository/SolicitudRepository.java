package com.tt1.trabajo.repository;
import com.tt1.trabajo.entity.SolicitudEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repositorio JPA para la entidad {@link SolicitudEntity}.
 * Gestiona el acceso a los datos de las simulaciones solicitadas.
 */
public interface SolicitudRepository extends JpaRepository<SolicitudEntity, Integer> {
    /**
     * Recupera una lista de solicitudes filtradas por el nombre de usuario.
     * @param username Nombre del usuario cuyas solicitudes se desean obtener.
     * @return Lista de {@link SolicitudEntity} asociadas al usuario.
     */
    List<SolicitudEntity> findByUsuarioUsername(String username);
}
