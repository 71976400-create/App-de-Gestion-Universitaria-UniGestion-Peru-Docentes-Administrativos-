package com.example.unigestionperu_docentesadministrativos.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nombre: String,
    val email: String,
    val password: String,
    val rol: String // "Docente", "Administrativo", "Estudiante"
)

@Entity(tableName = "cursos")
data class CursoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val codigo: String,
    val nombre: String,
    val docenteId: Long,
    val creditos: Int,
    val ciclo: String
)

@Entity(tableName = "matriculas")
data class MatriculaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val estudianteId: Long,
    val cursoId: Long,
    val nota1: Double = 0.0,
    val nota2: Double = 0.0,
    val examenFinal: Double = 0.0,
    val promedio: Double = 0.0
)
