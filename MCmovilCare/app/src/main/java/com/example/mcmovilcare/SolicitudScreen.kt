
package com.example.mcmovilcare

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SolicitudScreen(
    equipo: Equipo,
    onGuardar: (String) -> Unit,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    var descripcion by rememberSaveable {
        mutableStateOf("")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Nueva solicitud",
            style = MaterialTheme.typography.headlineMedium
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = equipo.nombre,
                    style = MaterialTheme.typography.titleLarge
                )

                Text(text = "Marca: ${equipo.marca}")
                Text(text = "Modelo: ${equipo.modelo}")
            }
        }

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = {
                Text("Describe el problema")
            },
            placeholder = {
                Text("Ej: Mi equipo no enciende")
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5
        )

        Button(
            onClick = {
                onGuardar(descripcion)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar solicitud")
        }

        Button(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }
}

