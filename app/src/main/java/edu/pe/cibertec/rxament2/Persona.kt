package edu.pe.cibertec.rxament2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personas")
data class Persona(
    @PrimaryKey val dni: String,
    val nombre: String,
    val telefono: String,
    val direccion: String,
    val estadoCivil: String?,
    val distrito: String?,
    val generoMasculino: Boolean,
    val generoFemenino: Boolean
)