package com.example.unigestionperu_docentesadministrativos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cursos")
data class CursoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val codigo: String,
    val nombre: String,
    val docenteId: Long,
    val creditos: Int,
    val ciclo: String,
    val facultad: String = "Ingeniería de Sistemas",
    val modalidad: String = "Presencial",
    val cupoMaximo: Int = 40,
    val matriculados: Int = 0
)
