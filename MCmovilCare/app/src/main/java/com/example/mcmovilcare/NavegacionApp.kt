
package com.example.mcmovilcare

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

    NavHost(
        navController = navController,
        startDestination = "equipos",
        modifier = modifier
    ) {
        // Pantalla principal: lista de equipos
        composable("equipos") {
            EquiposScreen(
                equipos = equipos,
                onEquipoClick = { equipo ->
                    navController.navigate("detalle/${equipo.id}")
                }
            )
        }

        // Pantalla de detalle
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
                        navController.navigate("solicitud/${equipo.id}")
                    }
                )
            }
        }

        // Pantalla para registrar solicitudes
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
                    onGuardar = { descripcion ->
                        println(
                            "Solicitud para ${equipo.nombre}: $descripcion"
                        )
                    },
                    onVolver = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

