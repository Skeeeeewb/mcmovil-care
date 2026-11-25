package com.example.mcmovilapi.repository;

import com.example.mcmovilapi.domain.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitudRepository extends JpaRepository<Solicitud, Integer> {

    List<Solicitud> findByEquipoId(Integer equipoId);
}
