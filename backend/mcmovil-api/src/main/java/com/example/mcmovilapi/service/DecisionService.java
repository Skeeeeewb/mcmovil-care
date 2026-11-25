package com.example.mcmovilapi.service;

import com.example.mcmovilapi.domain.DecisionCotizacion;
import com.example.mcmovilapi.domain.DecisionCotizacionEntity;
import com.example.mcmovilapi.repository.AtencionRepository;
import com.example.mcmovilapi.repository.DecisionCotizacionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DecisionService {

    private final AtencionRepository atencionRepository;
    private final DecisionCotizacionRepository repository;

    public DecisionService(AtencionRepository atencionRepository, DecisionCotizacionRepository repository) {
        this.atencionRepository = atencionRepository;
        this.repository = repository;
    }

    public DecisionCotizacionEntity obtener(Integer atencionId) {
        validarAtencion(atencionId);
        return repository.findById(atencionId)
                .orElseGet(() -> new DecisionCotizacionEntity(atencionId, DecisionCotizacion.PENDIENTE.name()));
    }

    public DecisionCotizacionEntity actualizar(Integer atencionId, String decision) {
        validarAtencion(atencionId);
        if (decision == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La decisión es obligatoria");
        }
        try {
            DecisionCotizacion.valueOf(decision);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La decisión debe ser una de: PENDIENTE, ACEPTADA, RECHAZADA");
        }
        return repository.save(new DecisionCotizacionEntity(atencionId, decision));
    }

    private void validarAtencion(Integer atencionId) {
        if (atencionId == null || !atencionRepository.existsById(atencionId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Atención no encontrada");
        }
    }
}
