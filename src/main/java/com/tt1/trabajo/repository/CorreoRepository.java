package com.tt1.trabajo.repository;

import com.tt1.trabajo.entity.CorreoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para acceder a los datos de la entidad {@link CorreoEntity}.
 */
@Repository
public interface CorreoRepository extends JpaRepository<CorreoEntity, Long> {
    /**
     * Busca los correos enviados por un usuario concreto, ordenados de más reciente a más antiguo.
     *
     * @param origen Nombre del usuario remitente.
     * @return Lista de correos enviados por el usuario.
     */
    List<CorreoEntity> findByOrigenOrderByFechaDesc(String origen);

    /**
     * Busca los correos recibidos por un usuario concreto, ordenados por fecha de más reciente a más antiguo.
     *
     * @param destino Nombre del usuario destinatario.
     * @return Lista de correos recibidos por el usuario.
     */
    List<CorreoEntity> findByDestinoOrderByFechaDesc(String destino);
}