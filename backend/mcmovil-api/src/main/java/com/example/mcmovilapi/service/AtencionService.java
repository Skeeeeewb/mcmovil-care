package com.example.mcmovilapi.service;

import com.example.mcmovilapi.domain.Atencion;
import com.example.mcmovilapi.domain.Equipo;
import com.example.mcmovilapi.repository.AtencionRepository;
import com.example.mcmovilapi.repository.EquipoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AtencionService {

    private final AtencionRepository repository;
    private final EquipoRepository equipoRepository;

    public AtencionService(AtencionRepository repository, EquipoRepository equipoRepository) {
        this.repository = repository;
        this.equipoRepository = equipoRepository;
    }

    public List<Atencion> listar() {
        return repository.findAll();
    }

    public List<Atencion> listarPorEquipo(Integer equipoId) {
        validarEquipo(equipoId);
        return repository.findByEquipoId(equipoId);
    }

    public Atencion obtener(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Atención no encontrada"));
    }

    public Atencion crear(Atencion atencion) {
        validarEquipo(atencion.getEquipoId());
        atencion.setId(null);
        return repository.save(atencion);
    }

    public Atencion actualizar(Integer id, Atencion atencion) {
        Atencion existente = obtener(id);
        validarEquipo(atencion.getEquipoId());
        existente.setEquipoId(atencion.getEquipoId());
        existente.setEstado(atencion.getEstado());
        existente.setDiagnostico(atencion.getDiagnostico());
        existente.setCotizacion(atencion.getCotizacion());
        return repository.save(existente);
    }

    public void eliminar(Integer id) {
        repository.delete(obtener(id));
    }

    private void validarEquipo(Integer equipoId) {
        Equipo equipo = equipoRepository.findById(equipoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Equipo no encontrado"));
        if (!"Disponible para retiro".equals(equipo.getEstado())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El equipo no está disponible para retiro");
        }
    }
}
