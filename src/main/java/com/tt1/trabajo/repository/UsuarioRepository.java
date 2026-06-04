package com.tt1.trabajo.repository;
import com.tt1.trabajo.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
/**
 * Repositorio JPA para la entidad {@link UsuarioEntity}.
 * Permite realizar búsquedas y operaciones sobre los usuarios registrados.
 */
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    boolean existsByUsername(String username);
    /**
     * Busca un usuario por su nombre de identificación único.
     * @param username El nombre de usuario a buscar.
     * @return Un {@link Optional} que contiene el usuario si se encuentra.
     */
    Optional<UsuarioEntity> findByUsername(String username);
}

