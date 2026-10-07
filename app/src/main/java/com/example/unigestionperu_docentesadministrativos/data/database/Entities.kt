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

@Entity(tableName = "salones")
data class SalonEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val codigo: String,         // Ej: "Aula A-101", "Lab-201"
    val edificio: String,       // Ej: "Pabellón A", "Pabellón B"
    val capacidad: Int = 40,    // Cupos Máximos
    val ocupados: Int = 0,      // Cupos Ocupados
    val tipo: String = "Teoría", // Ej: "Teoría", "Laboratorio", "Virtual"
    val docenteAsignado: String = "Sin Asignar", // Ej: "Dr. Carlos Mendoza"
    val horario: String = "Lun y Mié 08:00 - 10:00 AM" // Horario
)
