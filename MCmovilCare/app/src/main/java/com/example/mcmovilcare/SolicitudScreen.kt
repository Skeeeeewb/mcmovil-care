
package com.example.mcmovilcare

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import android.graphics.Bitmap

@Composable
fun SolicitudScreen(
    equipo: Equipo,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier,
    solicitudViewModel: SolicitudViewModel = viewModel()
) {
    val uiState by solicitudViewModel.uiState
        .collectAsStateWithLifecycle()

    // Imagen temporal tomada con la cámara
    var fotoTomada by remember {
        mutableStateOf<Bitmap?>(null)
    }

    // Abre la aplicación de cámara de Android
    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            fotoTomada = bitmap
        }
    }

    val solicitudesDelEquipo = uiState.solicitudes.filter {
        it.equipoId == equipo.id
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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

                Text("Marca: ${equipo.marca}")
                Text("Modelo: ${equipo.modelo}")
            }
        }

        OutlinedTextField(
            value = uiState.descripcion,
            onValueChange = {
                solicitudViewModel.actualizarDescripcion(it)
            },
            label = {
                Text("Describe el problema")
            },
            placeholder = {
                Text("Ej: Mi equipo no enciende")
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5,
            enabled = !uiState.guardando,
            isError = uiState.errorDescripcion != null,
            supportingText = {
                uiState.errorDescripcion?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = {
                if (uiState.errorDescripcion != null) {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = "Error de validación",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        // Nueva funcionalidad: cámara
        Text(
            text = "Fotografía del problema",
            style = MaterialTheme.typography.titleMedium
        )

        OutlinedButton(
            onClick = {
                camaraLauncher.launch(null)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Abrir cámara")
        }

        // Vista previa de la fotografía
        fotoTomada?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Fotografía del equipo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentScale = ContentScale.Fit
            )

            OutlinedButton(
                onClick = {
                    fotoTomada = null
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Eliminar fotografía")
            }
        }

        Button(
            onClick = {
                solicitudViewModel.guardarSolicitud(equipo.id)
            },
            enabled = !uiState.guardando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (uiState.guardando) {
                    "Guardando..."
                } else {
                    "Registrar solicitud"
                }
            )
        }

        uiState.mensaje?.let { mensaje ->
            Text(
                text = mensaje,
                color = if (
                    mensaje == "Solicitud registrada correctamente"
                ) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                }
            )
        }

        Text(
            text = "Solicitudes registradas",
            style = MaterialTheme.typography.titleLarge
        )

        if (solicitudesDelEquipo.isEmpty()) {
            Text("Este equipo todavía no tiene solicitudes.")
        } else {
            solicitudesDelEquipo.forEach { solicitud ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Solicitud #${solicitud.id}",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(solicitud.descripcion)

                        Text("Estado: ${solicitud.estado}")
                    }
                }
            }
        }

        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver")
        }
    }
}
