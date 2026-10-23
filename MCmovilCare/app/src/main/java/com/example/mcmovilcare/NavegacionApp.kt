
package com.example.mcmovilcare

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun NavegacionApp(
    equipos: List<Equipo>,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    // Atenciones de demostración del módulo de Moisés
    val atenciones = listOf(
        Atencion(
            id = 1001,
            equipoId = 1,
            estado = "En diagnóstico",
            diagnostico = "Posible falla de almacenamiento",
            cotizacion = 45000
        ),
        Atencion(
            id = 1002,
            equipoId = 2,
            estado = "En reparación",
            diagnostico = "Cambio de pantalla",
            cotizacion = 85000
        ),
        Atencion(
            id = 1003,
            equipoId = 3,
            estado = "Finalizada",
            diagnostico = "Revisión general completada",
            cotizacion = 20000
        )
    )

    NavHost(
        navController = navController,
        startDestination = "inicio",
        modifier = modifier
    ) {

        // Pantalla principal
        composable("inicio") {
            Column(
                modifier = Modifier
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
                    onClick = {
                        navController.navigate("equipos")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Mis equipos")
                }

                Button(
                    onClick = {
                        navController.navigate("atenciones")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Mis atenciones")
                }
            }
        }

        // Lista de equipos
        composable("equipos") {
            Column {
                OutlinedButton(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 8.dp
                    )
                ) {
                    Text("Volver al inicio")
                }

                EquiposScreen(
                    equipos = equipos,
                    onEquipoClick = { equipo ->
                        navController.navigate(
                            "detalle/${equipo.id}"
                        )
                    }
                )
            }
        }

        // Detalle de equipo
        composable(
            route = "detalle/{equipoId}",
            arguments = listOf(
                navArgument("equipoId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val equipoId = backStackEntry.arguments
                ?.getInt("equipoId")

            val equipo = equipos.find {
                it.id == equipoId
            }

            if (equipo != null) {
                DetalleEquipoScreen(
                    equipo = equipo,
                    onVolver = {
                        navController.popBackStack()
                    },
                    onSolicitarAtencion = {
                        navController.navigate(
                            "solicitud/${equipo.id}"
                        )
                    }
                )
            }
        }

        // Nueva solicitud conectada a Room
        composable(
            route = "solicitud/{equipoId}",
            arguments = listOf(
                navArgument("equipoId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val equipoId = backStackEntry.arguments
                ?.getInt("equipoId")

            val equipo = equipos.find {
                it.id == equipoId
            }

            if (equipo != null) {
                SolicitudScreen(
                    equipo = equipo,
                    onVolver = {
                        navController.popBackStack()
                    }
                )
            }
        }

        // Lista de atenciones de Moisés
        composable("atenciones") {
            Column {
                OutlinedButton(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 8.dp
                    )
                ) {
                    Text("Volver al inicio")
                }

                AtencionesScreen(
                    atenciones = atenciones,
                    onAtencionClick = { atencion ->
                        navController.navigate(
                            "detalleAtencion/${atencion.id}"
                        )
                    }
                )
            }
        }

        // Detalle de atención de Moisés
        composable(
            route = "detalleAtencion/{atencionId}",
            arguments = listOf(
                navArgument("atencionId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val atencionId = backStackEntry.arguments
                ?.getInt("atencionId")

            val atencion = atenciones.find {
                it.id == atencionId
            }

            if (atencion != null) {
                Column {
                    OutlinedButton(
                        onClick = {
                            navController.popBackStack()
                        },
                        modifier = Modifier.padding(
                            start = 16.dp,
                            top = 8.dp
                        )
                    ) {
                        Text("Volver a mis atenciones")
                    }

                    DetalleAtencionScreen(
                        atencion = atencion,
                        onAceptar = {
                            println(
                                "Cotización aceptada: ${atencion.id}"
                            )
                        },
                        onRechazar = {
                            println(
                                "Cotización rechazada: ${atencion.id}"
                            )
                        }
                    )
                }
            }
        }
    }
}
