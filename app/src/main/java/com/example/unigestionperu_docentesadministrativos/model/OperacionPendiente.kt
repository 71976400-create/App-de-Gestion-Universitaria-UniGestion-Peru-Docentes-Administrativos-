package com.example.unigestionperu_docentesadministrativos.model

data class OperacionPendiente(
    val id: Long = 0L,
    val uuidOperacion: String,
    val entidad: String,
    val idEntidadLocal: Long,
    val tipoOperacion: String, // "INSERT", "UPDATE", "DELETE"
    val payload: String, // JSON payload
    val fechaRegistro: Long = System.currentTimeMillis(),
    val estado: String = EstadoSincronizacion.PENDIENTE.name,
    val intentos: Int = 0,
    val mensajeError: String? = null
)
