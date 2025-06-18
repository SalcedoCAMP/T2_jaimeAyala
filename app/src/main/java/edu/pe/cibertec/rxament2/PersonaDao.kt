package edu.pe.cibertec.rxament2.data

import androidx.room.*

@Dao
interface PersonaDao {
    @Query("SELECT * FROM personas")
    suspend fun getAll(): List<Persona>

    @Query("SELECT * FROM personas WHERE nombre LIKE :query OR dni LIKE :query")
    suspend fun search(query: String): List<Persona>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(persona: Persona)

    @Delete
    suspend fun delete(persona: Persona)

    @Query("SELECT COUNT(*) FROM personas WHERE dni = :dni")
    suspend fun exists(dni: String): Int
}