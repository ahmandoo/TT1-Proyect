package com.tt1.trabajo.repository;
import com.tt1.trabajo.entity.SolicitudEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SolicitudRepository extends JpaRepository<SolicitudEntity, Integer> {
    List<SolicitudEntity> findByUsuarioUsername(String username);
}
