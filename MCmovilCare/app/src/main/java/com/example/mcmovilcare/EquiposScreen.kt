
package com.example.mcmovilcare

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EquiposScreen(
    equipos: List<Equipo>,
    onEquipoClick: (Equipo) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "MCmovil Care",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        item {
            Text(
                text = "Mis equipos",
                style = MaterialTheme.typography.titleLarge
            )
        }

        items(equipos, key = { equipo -> equipo.id }) { equipo ->
            TarjetaEquipo(
                equipo = equipo,
                onClick = { onEquipoClick(equipo) }
            )
        }
    }
}

@Composable
fun TarjetaEquipo(
    equipo: Equipo,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = equipo.nombre,
                style = MaterialTheme.typography.titleMedium
            )

            Text(text = "Marca: ${equipo.marca}")
            Text(text = "Modelo: ${equipo.modelo}")
            Text(text = "Estado: ${equipo.estado}")
        }
    }
}
