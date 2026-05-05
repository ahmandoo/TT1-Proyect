package com.tt1.trabajo.repository;

import com.tt1.trabajo.entity.CorreoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CorreoRepository extends JpaRepository<CorreoEntity, Long> {

    List<CorreoEntity> findByOrigenOrderByFechaDesc(String origen);

    List<CorreoEntity> findByDestinoOrderByFechaDesc(String destino);
}