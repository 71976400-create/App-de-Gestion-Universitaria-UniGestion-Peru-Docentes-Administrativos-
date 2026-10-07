package com.example.unigestionperu_docentesadministrativos.model

data class Matricula(
    val id: Long = 0L,
    val estudianteId: Long,
    val cursoId: Long,
    val nota1: Double = 0.0,
    val nota2: Double = 0.0,
    val examenFinal: Double = 0.0,
    val promedio: Double = 0.0
)
