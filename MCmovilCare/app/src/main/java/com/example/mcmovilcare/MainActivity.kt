package com.example.mcmovilcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.mcmovilcare.ui.NavegacionApp
import com.example.mcmovilcare.ui.theme.MCmovilCareTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MCmovilCareTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavegacionApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
