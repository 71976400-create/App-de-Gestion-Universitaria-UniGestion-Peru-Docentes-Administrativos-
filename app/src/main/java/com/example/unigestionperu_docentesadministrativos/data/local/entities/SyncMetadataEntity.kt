package com.example.unigestionperu_docentesadministrativos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_metadata")
data class SyncMetadataEntity(
    @PrimaryKey
    val recurso: String, // "cursos", "matriculas", "salones", "usuarios"
    val ultimaSincronizacionExitosa: Long = System.currentTimeMillis(),
    val cursorRemoto: String? = null
)
