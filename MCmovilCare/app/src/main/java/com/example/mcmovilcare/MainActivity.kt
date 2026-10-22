
package com.example.mcmovilcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.mcmovilcare.ui.theme.MCmovilCareTheme

class MainActivity : ComponentActivity() {

    private val equiposDeEjemplo = listOf(
        Equipo(
            id = 1,
            nombre = "Notebook personal",
            marca = "Lenovo",
            modelo = "IdeaPad 3",
            estado = "En diagnóstico"
        ),
        Equipo(
            id = 2,
            nombre = "Teléfono",
            marca = "Samsung",
            modelo = "Galaxy A54",
            estado = "En reparación"
        ),
        Equipo(
            id = 3,
            nombre = "Tablet",
            marca = "Samsung",
            modelo = "Galaxy Tab A8",
            estado = "Disponible para retiro"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MCmovilCareTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    NavegacionApp(
                        equipos = equiposDeEjemplo,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
