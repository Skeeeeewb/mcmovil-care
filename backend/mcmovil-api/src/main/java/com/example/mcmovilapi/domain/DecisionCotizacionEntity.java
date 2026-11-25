package com.example.mcmovilapi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "decisiones_cotizacion")
public class DecisionCotizacionEntity {

    @Id
    @Column(name = "atencion_id")
    private Integer atencionId;

    @Column(nullable = false)
    private String decision;

    public DecisionCotizacionEntity() {
    }

    public DecisionCotizacionEntity(Integer atencionId, String decision) {
        this.atencionId = atencionId;
        this.decision = decision;
    }

    public Integer getAtencionId() {
        return atencionId;
    }

    public void setAtencionId(Integer atencionId) {
        this.atencionId = atencionId;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }
}
