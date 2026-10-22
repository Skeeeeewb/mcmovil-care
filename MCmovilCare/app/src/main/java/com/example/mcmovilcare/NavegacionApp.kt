
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
        composable("equipos") {
            EquiposScreen(
                equipos = equipos,
                onEquipoClick = { equipo ->
                    navController.navigate("detalle/${equipo.id}")
                }
            )
        }

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
                    }
                )
            }
        }
    }
}
