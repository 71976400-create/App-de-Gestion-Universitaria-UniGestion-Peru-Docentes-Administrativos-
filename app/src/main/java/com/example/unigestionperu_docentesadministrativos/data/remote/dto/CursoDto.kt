package com.example.unigestionperu_docentesadministrativos.data.remote.dto

data class CursoDto(
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
