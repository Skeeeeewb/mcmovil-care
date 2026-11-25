package com.example.mcmovilapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "atenciones")
public class Atencion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Column(name = "equipo_id", nullable = false)
    private Integer equipoId;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private String diagnostico;

    @Column(nullable = false)
    private Integer cotizacion;

    public Atencion() {
    }

    public Atencion(Integer id, Integer equipoId, String estado, String diagnostico, Integer cotizacion) {
        this.id = id;
        this.equipoId = equipoId;
        this.estado = estado;
        this.diagnostico = diagnostico;
        this.cotizacion = cotizacion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEquipoId() {
        return equipoId;
    }

    public void setEquipoId(Integer equipoId) {
        this.equipoId = equipoId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public Integer getCotizacion() {
        return cotizacion;
    }

    public void setCotizacion(Integer cotizacion) {
        this.cotizacion = cotizacion;
    }
}
