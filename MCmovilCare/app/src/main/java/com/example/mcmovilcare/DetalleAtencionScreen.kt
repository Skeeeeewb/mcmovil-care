package com.example.mcmovilcare

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mcmovilcare.ui.theme.MCmovilCareTheme

@Composable
fun DetalleAtencionScreen(
    atencion: Atencion,
    modifier: Modifier = Modifier,
    decisiones: Map<Int, DecisionCotizacion> = emptyMap(),
    onAceptar: () -> Unit = {},
    onRechazar: () -> Unit = {}
) {
    // Guarda temporalmente la imagen seleccionada.
    var imagenUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val context = LocalContext.current

    // Abre el selector de imágenes del sistema Android.
    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imagenUri = uri
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

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Obtenemos la decisión actual de la cotización.
                val decision = decisiones[atencion.id]
                    ?: DecisionCotizacion.PENDIENTE

                Text(
                    text = "Atención N.º ${atencion.id}",
                    style = MaterialTheme.typography.titleLarge
                )

                Text(text = "Equipo ID: ${atencion.equipoId}")
                Text(text = "Estado: ${atencion.estado}")

                Text(
                    text = "Diagnóstico",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(text = atencion.diagnostico)

                Text(
                    text = "Cotización",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(text = "$${atencion.cotizacion}")

                Text(
                    text = "Decisión: ${decision.name}",
                    style = MaterialTheme.typography.titleMedium
                )

                // Botones de aceptación y rechazo.
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onAceptar,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Aceptar")
                    }

                    OutlinedButton(
                        onClick = onRechazar,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Rechazar")
                    }
                }

                // Botón independiente para abrir la galería.
                Button(
                    onClick = {
                        galeriaLauncher.launch("image/*")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Seleccionar evidencia")
                }

                // Mostramos la imagen debajo del botón.
                imagenUri?.let { uri ->

                    val bitmap = remember(uri) {
                        try {
                            context.contentResolver
                                .openInputStream(uri)?.use { input ->
                                    BitmapFactory.decodeStream(input)
                                }
                        } catch (e: Exception) {
                            null
                        }
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

// Vista previa sencilla.
@Preview(showBackground = true)
@Composable
fun DetalleAtencionPreview() {
    val atencionEjemplo = Atencion(
        id = 1001,
        equipoId = 1,
        estado = "En diagnóstico",
        diagnostico = "Posible falla de almacenamiento",
        cotizacion = 45000
    )

    MCmovilCareTheme {
        DetalleAtencionScreen(
            atencion = atencionEjemplo
        )
    }
}

// Conecta la pantalla con el ViewModel.
@Composable
fun DetalleAtencionConEstado(
    atencion: Atencion,
    viewModel: AtencionesViewModel
) {
    val decisiones = viewModel.decisiones.collectAsState()

    DetalleAtencionScreen(
        atencion = atencion,
        decisiones = decisiones.value,
        onAceptar = {
            viewModel.aceptarCotizacion(atencion.id)
        },
        onRechazar = {
            viewModel.rechazarCotizacion(atencion.id)
        }
    )
}

// Vista previa para probar los botones y la galería.
@Preview(showBackground = true)
@Composable
fun DetalleInteractivoPreview() {
    val viewModel = remember {
        AtencionesViewModel()
    }

    val atencionEjemplo = Atencion(
        id = 1001,
        equipoId = 1,
        estado = "En diagnóstico",
        diagnostico = "Posible falla de almacenamiento",
        cotizacion = 45000
    )

    MCmovilCareTheme {
        DetalleAtencionConEstado(
            atencion = atencionEjemplo,
            viewModel = viewModel
        )
    }
}
