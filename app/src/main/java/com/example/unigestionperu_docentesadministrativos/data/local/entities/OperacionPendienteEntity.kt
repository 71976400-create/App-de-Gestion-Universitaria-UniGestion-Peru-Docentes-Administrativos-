package com.example.unigestionperu_docentesadministrativos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "operaciones_pendientes")
data class OperacionPendienteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val uuidOperacion: String,
    val entidad: String,
    val idEntidadLocal: Long,
    val tipoOperacion: String, // INSERT, UPDATE, DELETE
    val payload: String,
    val fechaRegistro: Long = System.currentTimeMillis(),
    val estado: String = "PENDIENTE", // PENDIENTE, ENVIANDO, SINCRONIZADO, ERROR
    val intentos: Int = 0,
    val mensajeError: String? = null
)
