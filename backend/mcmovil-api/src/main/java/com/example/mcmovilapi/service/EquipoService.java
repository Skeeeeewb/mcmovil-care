package com.example.mcmovilapi.service;

import com.example.mcmovilapi.domain.Equipo;
import com.example.mcmovilapi.repository.EquipoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EquipoService {

    private final EquipoRepository repository;

    public EquipoService(EquipoRepository repository) {
        this.repository = repository;
    }

    public List<Equipo> listar() {
        return repository.findAll();
    }

    public Equipo obtener(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipo no encontrado"));
    }

    public Equipo crear(Equipo equipo) {
        equipo.setId(null);
        return repository.save(equipo);
    }

    public Equipo actualizar(Integer id, Equipo equipo) {
        Equipo existente = obtener(id);
        existente.setNombre(equipo.getNombre());
        existente.setMarca(equipo.getMarca());
        existente.setModelo(equipo.getModelo());
        existente.setEstado(equipo.getEstado());
        return repository.save(existente);
    }

    public void eliminar(Integer id) {
        repository.delete(obtener(id));
    }
}
