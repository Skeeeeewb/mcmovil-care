package com.example.mcmovilcare.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mcmovilcare.viewmodel.AtencionesViewModel
import com.example.mcmovilcare.viewmodel.DetalleAtencionViewModel
import com.example.mcmovilcare.viewmodel.DetalleEquipoViewModel
import com.example.mcmovilcare.viewmodel.EquiposViewModel
import com.example.mcmovilcare.viewmodel.SolicitudViewModel

private object Rutas {
    const val INICIO = "inicio"
    const val EQUIPOS = "equipos"
    const val ATENCIONES = "atenciones"
    const val DETALLE_EQUIPO = "detalle/{equipoId}"
    const val SOLICITUD = "solicitud/{equipoId}"
    const val DETALLE_ATENCION = "detalleAtencion/{atencionId}"
    const val ARG_EQUIPO_ID = "equipoId"
    const val ARG_ATENCION_ID = "atencionId"

    fun detalleEquipo(id: Int) = "detalle/$id"

    fun solicitud(id: Int) = "solicitud/$id"

    fun detalleAtencion(id: Int) = "detalleAtencion/$id"
}

@Composable
fun NavegacionApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Rutas.INICIO,
        modifier = modifier
    ) {
        composable(Rutas.INICIO) {
            InicioScreen(
                onEquiposClick = { navController.navigate(Rutas.EQUIPOS) },
                onAtencionesClick = { navController.navigate(Rutas.ATENCIONES) }
            )
        }

        composable(Rutas.EQUIPOS) {
            val viewModel: EquiposViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            Column {
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp)
                ) {
                    Text("Volver al inicio")
                }

                EquiposScreen(
                    uiState = uiState,
                    onEquipoClick = { navController.navigate(Rutas.detalleEquipo(it)) }
                )
            }
        }

        composable(
            route = Rutas.DETALLE_EQUIPO,
            arguments = listOf(
                navArgument(Rutas.ARG_EQUIPO_ID) { type = NavType.IntType }
            )
        ) { entry ->
            val equipoId = entry.arguments?.getInt(Rutas.ARG_EQUIPO_ID)

            if (equipoId != null) {
                val viewModel: DetalleEquipoViewModel = viewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(equipoId) {
                    viewModel.cargarEquipo(equipoId)
                }

                DetalleEquipoScreen(
                    uiState = uiState,
                    onSolicitarAtencion = { navController.navigate(Rutas.solicitud(it)) },
                    onVolver = { navController.popBackStack() }
                )
            }
        }

        composable(
            route = Rutas.SOLICITUD,
            arguments = listOf(
                navArgument(Rutas.ARG_EQUIPO_ID) { type = NavType.IntType }
            )
        ) { entry ->
            val equipoId = entry.arguments?.getInt(Rutas.ARG_EQUIPO_ID)

            if (equipoId != null) {
                val viewModel: SolicitudViewModel = viewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(equipoId) {
                    viewModel.cargarEquipo(equipoId)
                }

                SolicitudScreen(
                    uiState = uiState,
                    onEvent = viewModel::onEvent,
                    onVolver = { navController.popBackStack() }
                )
            }
        }

        composable(Rutas.ATENCIONES) {
            val viewModel: AtencionesViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            Column {
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp)
                ) {
                    Text("Volver al inicio")
                }

                AtencionesScreen(
                    uiState = uiState,
                    onAtencionClick = { navController.navigate(Rutas.detalleAtencion(it)) }
                )
            }
        }

        composable(
            route = Rutas.DETALLE_ATENCION,
            arguments = listOf(
                navArgument(Rutas.ARG_ATENCION_ID) { type = NavType.IntType }
            )
        ) { entry ->
            val atencionId = entry.arguments?.getInt(Rutas.ARG_ATENCION_ID)

            if (atencionId != null) {
                val viewModel: DetalleAtencionViewModel = viewModel()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                LaunchedEffect(atencionId) {
                    viewModel.cargarAtencion(atencionId)
                }

                Column {
                    OutlinedButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(start = 16.dp, top = 8.dp)
                    ) {
                        Text("Volver a mis atenciones")
                    }

                    DetalleAtencionScreen(
                        uiState = uiState,
                        onEvent = viewModel::onEvent
                    )
                }
            }
        }
    }
}
