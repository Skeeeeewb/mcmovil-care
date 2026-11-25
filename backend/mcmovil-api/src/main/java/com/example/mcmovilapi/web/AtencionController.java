package com.example.mcmovilapi.web;

import com.example.mcmovilapi.domain.Atencion;
import com.example.mcmovilapi.service.AtencionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/atenciones")
public class AtencionController {

    private final AtencionService service;

    public AtencionController(AtencionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Atencion> listar() {
        return service.listar();
    }

    @GetMapping("/equipo/{equipoId}")
    public List<Atencion> listarPorEquipo(@PathVariable Integer equipoId) {
        return service.listarPorEquipo(equipoId);
    }

    @GetMapping("/{id}")
    public Atencion obtener(@PathVariable Integer id) {
        return service.obtener(id);
    }

    @PostMapping
    public ResponseEntity<Atencion> crear(@RequestBody Atencion atencion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(atencion));
    }

    @PutMapping("/{id}")
    public Atencion actualizar(@PathVariable Integer id, @RequestBody Atencion atencion) {
        return service.actualizar(id, atencion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
