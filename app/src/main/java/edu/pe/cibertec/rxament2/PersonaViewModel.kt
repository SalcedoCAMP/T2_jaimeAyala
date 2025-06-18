package edu.pe.cibertec.rxament2.ui

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.pe.cibertec.rxament2.data.AppDatabase
import edu.pe.cibertec.rxament2.data.Persona
import kotlinx.coroutines.launch

class PersonaViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = AppDatabase.getInstance(app).personaDao()
    var personas by mutableStateOf<List<Persona>>(emptyList())
        private set

    var mensaje by mutableStateOf("")
    var mostrarSnackbar by mutableStateOf(false)

    fun cargarPersonas() {
        viewModelScope.launch {
            personas = dao.getAll()
        }
    }

    fun buscar(query: String) {
        viewModelScope.launch {
            personas = dao.search("%$query%")
            mensaje = "Encontrados: ${personas.size}"
            mostrarSnackbar = true
        }
    }

    fun insertar(persona: Persona, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            if (dao.exists(persona.dni) > 0) {
                onError("El DNI ya existe")
            } else {
                dao.insert(persona)
                onSuccess()
            }
        }
    }

    fun eliminar(persona: Persona) {
        viewModelScope.launch {
            dao.delete(persona)
            cargarPersonas()
        }
    }
}
