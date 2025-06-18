package edu.pe.cibertec.rxament2.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.pe.cibertec.rxament2.data.Persona
import edu.pe.cibertec.rxament2.ui.PersonaViewModel

@Composable
fun RegistroScreen(
    navController: NavController,
    dni: String?,
    viewModel: PersonaViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val ctx = LocalContext.current
    var isEditMode by remember { mutableStateOf(false) }

    var nombre by remember { mutableStateOf("") }
    var nuevoDni by remember { mutableStateOf(dni ?: "") }
    var direccion by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var estadoCivil by remember { mutableStateOf("") }
    var distrito by remember { mutableStateOf("") }
    var masculino by remember { mutableStateOf(false) }
    var femenino by remember { mutableStateOf(false) }

    LaunchedEffect(dni, viewModel.personas) {
        if (viewModel.personas.isEmpty()) {
            viewModel.cargarPersonas()
        }

        if (!dni.isNullOrEmpty()) {
            isEditMode = true
            val persona = viewModel.personas.find { it.dni == dni }
            if (persona != null) {
                nombre = persona.nombre
                nuevoDni = persona.dni
                direccion = persona.direccion
                telefono = persona.telefono
                estadoCivil = persona.estadoCivil ?: ""
                distrito = persona.distrito ?: ""
                masculino = persona.generoMasculino
                femenino = persona.generoFemenino
            }
        }
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre Completo") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = nuevoDni, onValueChange = { nuevoDni = it }, label = { Text("DNI") }, enabled = !isEditMode, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(8.dp))
        Text("Estado Civil:")
        Row {
            listOf("Soltero(a)", "Casado(a)", "Divorciado(a)", "Viudo(a)").forEach { estado ->
                Row(Modifier.padding(4.dp)) {
                    RadioButton(selected = estadoCivil == estado, onClick = { estadoCivil = estado })
                    Text(estado)
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        Text("Distrito:")
        val distritos = listOf("San Isidro", "Miraflores", "Surco", "La Molina", "Barranco", "San Borja")
        DropdownMenuBox(distrito, distritos) { distrito = it }

        Row {
            Checkbox(checked = masculino, onCheckedChange = { masculino = it })
            Text("Masculino")
            Checkbox(checked = femenino, onCheckedChange = { femenino = it })
            Text("Femenino")
        }

        Button(onClick = {
            if (nombre.isBlank() || nuevoDni.isBlank() || direccion.isBlank() || telefono.isBlank()) {
                Toast.makeText(ctx, "Complete todos los campos obligatorios", Toast.LENGTH_LONG).show()
                return@Button
            }

            val persona = Persona(nuevoDni, nombre, telefono, direccion, estadoCivil, distrito, masculino, femenino)

            viewModel.insertar(persona, {
                Toast.makeText(ctx, if (isEditMode) "Editado correctamente" else "Registrado", Toast.LENGTH_SHORT).show()
                navController.popBackStack()
                viewModel.cargarPersonas()
            }, {
                Toast.makeText(ctx, it, Toast.LENGTH_LONG).show()
            })
        }) {
            Text(if (isEditMode) "GUARDAR CAMBIOS" else "GRABAR")
        }
    }
}

@Composable
fun DropdownMenuBox(selected: String, items: List<String>, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            label = { Text("Distrito") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth().clickable { expanded = true }
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach { item ->
                DropdownMenuItem(text = { Text(item) }, onClick = {
                    onSelected(item)
                    expanded = false
                })
            }
        }
    }
}
