package com.example.mcmovilapi.service;

import com.example.mcmovilapi.domain.Solicitud;
import com.example.mcmovilapi.repository.EquipoRepository;
import com.example.mcmovilapi.repository.SolicitudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SolicitudService {

    private static final String ESTADOS_PERMITIDOS = "Pendiente, Aceptada, Rechazada";

    private final SolicitudRepository repository;
    private final EquipoRepository equipoRepository;

    public SolicitudService(SolicitudRepository repository, EquipoRepository equipoRepository) {
        this.repository = repository;
        this.equipoRepository = equipoRepository;
    }

    public List<Solicitud> listar() {
        return repository.findAll();
    }

    public List<Solicitud> listarPorEquipo(Integer equipoId) {
        validarEquipo(equipoId);
        return repository.findByEquipoId(equipoId);
    }

    public Solicitud obtener(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud no encontrada"));
    }

    public Solicitud crear(Solicitud solicitud) {
        validarEquipo(solicitud.getEquipoId());
        validarDescripcion(solicitud.getDescripcion());
        validarEstado(solicitud.getEstado());
        solicitud.setId(null);
        return repository.save(solicitud);
    }

    public Solicitud actualizar(Integer id, Solicitud solicitud) {
        Solicitud existente = obtener(id);
        validarEquipo(solicitud.getEquipoId());
        validarDescripcion(solicitud.getDescripcion());
        validarEstado(solicitud.getEstado());
        existente.setEquipoId(solicitud.getEquipoId());
        existente.setDescripcion(solicitud.getDescripcion());
        existente.setEstado(solicitud.getEstado());
        return repository.save(existente);
    }

    public void eliminar(Integer id) {
        repository.delete(obtener(id));
    }

    private void validarEquipo(Integer equipoId) {
        if (equipoId == null || !equipoRepository.existsById(equipoId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Equipo no encontrado");
        }
    }

    private void validarDescripcion(String descripcion) {
        if (descripcion == null || descripcion.trim().length() < 10) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La descripción debe tener al menos 10 caracteres");
        }
    }

    private void validarEstado(String estado) {
        if (estado == null || (!estado.equals("Pendiente") && !estado.equals("Aceptada")
                && !estado.equals("Rechazada"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El estado debe ser uno de: " + ESTADOS_PERMITIDOS);
        }
    }
}
