package com.example.unigestionperu_docentesadministrativos.data.remote.dto

data class OperacionPendienteDto(
    val uuidOperacion: String,
    val entidad: String,
    val idEntidadLocal: Long,
    val tipoOperacion: String,
    val payload: String,
    val fechaRegistro: Long
)

data class SyncRequestDto(
    val operaciones: List<OperacionPendienteDto>
)

data class SyncResultDto(
    val uuidOperacion: String,
    val exito: Boolean,
    val idRemoto: Long? = null,
    val mensaje: String? = null
)

data class SyncResponseDto(
    val resultados: List<SyncResultDto>
)
