package com.example.mcmovilapi.repository;

import com.example.mcmovilapi.domain.Atencion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AtencionRepository extends JpaRepository<Atencion, Integer> {

    List<Atencion> findByEquipoId(Integer equipoId);
}
