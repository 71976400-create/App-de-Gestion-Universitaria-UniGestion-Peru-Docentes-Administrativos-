package com.example.unigestionperu_docentesadministrativos.data.remote.dto

data class SalonDto(
    val id: Long = 0L,
    val codigo: String,
    val edificio: String,
    val capacidad: Int = 40,
    val ocupados: Int = 0,
    val tipo: String = "Teoría",
    val docenteAsignado: String = "Sin Asignar",
    val horario: String = "Lun y Mié 08:00 - 10:00 AM"
)
