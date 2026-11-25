package com.example.mcmovilapi.web;

import com.example.mcmovilapi.domain.DecisionCotizacionEntity;
import com.example.mcmovilapi.service.DecisionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/atenciones/{atencionId}/decision")
public class DecisionController {

    private final DecisionService service;

    public DecisionController(DecisionService service) {
        this.service = service;
    }

    @GetMapping
    public DecisionCotizacionEntity obtener(@PathVariable Integer atencionId) {
        return service.obtener(atencionId);
    }

    @PutMapping
    public DecisionCotizacionEntity actualizar(@PathVariable Integer atencionId,
                                               @RequestBody Map<String, String> body) {
        return service.actualizar(atencionId, body.get("decision"));
    }
}
