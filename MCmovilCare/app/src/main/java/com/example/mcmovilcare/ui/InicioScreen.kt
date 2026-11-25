package com.example.mcmovilcare.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun InicioScreen(
    onEquiposClick: () -> Unit,
    onAtencionesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "MCmovil Care",
            style = MaterialTheme.typography.headlineMedium
        )

        Text("Selecciona una opción")

        Button(
            onClick = onEquiposClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Mis equipos")
        }

        Button(
            onClick = onAtencionesClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Mis atenciones")
        }
    }
}
