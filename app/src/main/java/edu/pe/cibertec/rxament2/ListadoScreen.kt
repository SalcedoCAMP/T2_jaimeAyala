// ListadoScreen.kt
package edu.pe.cibertec.rxament2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.pe.cibertec.rxament2.data.Persona
import edu.pe.cibertec.rxament2.ui.PersonaViewModel

@Composable
fun ListadoScreen(
    navController: NavController,
    viewModel: PersonaViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var query by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf<Pair<Boolean, Persona?>>(false to null) }

    LaunchedEffect(Unit) {
        viewModel.cargarPersonas()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Padrón de Personas", style = MaterialTheme.typography.headlineMedium)
        Text("Gestione el registro de personas en el sistema")

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Buscar por nombre o documento...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Button(onClick = { viewModel.buscar(query) }) {
            Text("Consultar")
        }

        Spacer(Modifier.height(8.dp))

        Button(onClick = { navController.navigate("registro") }) {
            Text("Registrar Persona")
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(viewModel.personas) { persona ->
                Card(modifier = Modifier.padding(8.dp).fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Nombre: ${persona.nombre}")
                        Text("Documento: ${persona.dni}")
                        Text("Teléfono: ${persona.telefono}")
                        Text("Dirección: ${persona.direccion}")
                        persona.distrito?.let { Text("Distrito: $it") }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                            IconButton(onClick = {
                                navController.navigate("registro/${persona.dni}")
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar")
                            }
                            IconButton(onClick = {
                                showDialog = true to persona
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog.first) {
        AlertDialog(
            onDismissRequest = { showDialog = false to null },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Estás seguro de eliminar a ${showDialog.second?.nombre}?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog.second?.let { viewModel.eliminar(it) }
                    showDialog = false to null
                }) {
                    Text("Sí")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false to null }) {
                    Text("No")
                }
            }
        )
    }

    if (viewModel.mostrarSnackbar) {
        Snackbar(
            modifier = Modifier.padding(8.dp),
            action = {
                Button(onClick = { viewModel.mostrarSnackbar = false }) {
                    Text("OK")
                }
            }
        ) {
            Text(viewModel.mensaje)
        }
    }
}
