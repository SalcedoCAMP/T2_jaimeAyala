package edu.pe.cibertec.rxament2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.pe.cibertec.rxament2.ui.screens.ListadoScreen
import edu.pe.cibertec.rxament2.ui.screens.RegistroScreen

import edu.pe.cibertec.rxament2.ui.theme.RxamenT2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RxamenT2Theme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = "listado",
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        composable("listado") { ListadoScreen(navController) }
                        composable("registro/{dni}") { backStackEntry ->
                            val dni = backStackEntry.arguments?.getString("dni")
                            RegistroScreen(navController, dni)
                        }
                        composable("registro") {
                            RegistroScreen(navController, null)
                        }
                    }
                }
            }
        }
    }
}
