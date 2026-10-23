
package com.example.mcmovilcare

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "decisiones_cotizacion")
data class DecisionCotizacionEntity(
    @PrimaryKey
    val atencionId: Int,
    val decision: String
)
