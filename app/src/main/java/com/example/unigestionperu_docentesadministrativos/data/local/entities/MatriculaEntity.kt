package com.example.unigestionperu_docentesadministrativos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

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
