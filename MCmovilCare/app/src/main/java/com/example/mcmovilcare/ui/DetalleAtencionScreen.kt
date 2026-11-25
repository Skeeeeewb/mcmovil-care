package com.example.mcmovilcare.ui

import android.Manifest
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mcmovilcare.viewmodel.DetalleAtencionUiEvent
import com.example.mcmovilcare.viewmodel.DetalleAtencionUiState

@Composable
fun DetalleAtencionScreen(
    uiState: DetalleAtencionUiState,
    onEvent: (DetalleAtencionUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            onEvent(DetalleAtencionUiEvent.ImagenSeleccionada(uri.toString()))
        }
    }

    val permisoGaleria = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val permisoGaleriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            galeriaLauncher.launch("image/*")
        } else {
            onEvent(DetalleAtencionUiEvent.PermisoGaleriaDenegado)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Detalle de atención",
            style = MaterialTheme.typography.headlineMedium
        )

        uiState.atencion?.let { atencion ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Atención N.º ${atencion.id}",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(text = "Equipo ID: ${atencion.equipoId}")
                    Text(text = "Estado: ${atencion.estado}")

                    Button(
                        onClick = {
                            onEvent(DetalleAtencionUiEvent.AlternarDiagnostico)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            if (uiState.mostrarDiagnostico) {
                                "Ocultar diagnóstico"
                            } else {
                                "Mostrar diagnóstico"
                            }
                        )
                    }

                    AnimatedVisibility(
                        visible = uiState.mostrarDiagnostico,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column {
                            Text(
                                text = "Diagnóstico",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(text = atencion.diagnostico)
                        }
                    }

                    Text(
                        text = "Cotización",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(text = "$${atencion.cotizacion}")

                    Text(
                        text = "Decisión: ${uiState.decision.name}",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                onEvent(DetalleAtencionUiEvent.AceptarCotizacion)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Aceptar")
                        }

                        OutlinedButton(
                            onClick = {
                                onEvent(DetalleAtencionUiEvent.RechazarCotizacion)
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Rechazar")
                        }
                    }

                    Button(
                        onClick = { permisoGaleriaLauncher.launch(permisoGaleria) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Seleccionar evidencia")
                    }

                    if (uiState.permisoGaleriaDenegado) {
                        Text(
                            text = "Permiso de galería denegado. Habilítalo en Ajustes para adjuntar la evidencia.",
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    uiState.imagenUri?.let { uriString ->
                        val uri = Uri.parse(uriString)
                        val bitmap = remember(uri) {
                            runCatching {
                                context.contentResolver
                                    .openInputStream(uri)?.use { input ->
                                        BitmapFactory.decodeStream(input)
                                    }
                            }.getOrNull()
                        }

                        if (bitmap != null) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Evidencia de la atención",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp),
                                contentScale = ContentScale.Fit
                            )
                        } else {
                            Text("No se pudo cargar la imagen seleccionada")
                        }
                    }
                }
            }
        }
    }
}
