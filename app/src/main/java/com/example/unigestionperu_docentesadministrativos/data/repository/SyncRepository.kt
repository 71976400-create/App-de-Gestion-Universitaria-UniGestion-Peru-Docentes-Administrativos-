package com.example.unigestionperu_docentesadministrativos.data.repository

import com.example.unigestionperu_docentesadministrativos.data.local.dao.OperacionPendienteDao
import com.example.unigestionperu_docentesadministrativos.data.local.dao.SyncMetadataDao
import com.example.unigestionperu_docentesadministrativos.data.local.entities.OperacionPendienteEntity
import com.example.unigestionperu_docentesadministrativos.data.local.entities.SyncMetadataEntity
import com.example.unigestionperu_docentesadministrativos.data.remote.api.ApiService
import com.example.unigestionperu_docentesadministrativos.data.remote.dto.OperacionPendienteDto
import com.example.unigestionperu_docentesadministrativos.data.remote.dto.SyncRequestDto
import kotlinx.coroutines.flow.Flow

class SyncRepository(
    private val operacionPendienteDao: OperacionPendienteDao,
    private val syncMetadataDao: SyncMetadataDao,
    private val apiService: ApiService
) {
    fun observeOperacionesPendientes(): Flow<List<OperacionPendienteEntity>> {
        return operacionPendienteDao.observeAllOperaciones()
    }

    fun observeCountPendientes(): Flow<Int> {
        return operacionPendienteDao.observeCountPendientes()
    }

    fun observeMetadata(): Flow<List<SyncMetadataEntity>> {
        return syncMetadataDao.observeAllMetadata()
    }

    suspend fun sincronizar(): Boolean {
        val pendientes = operacionPendienteDao.getOperacionesPendientes()
        if (pendientes.isEmpty()) {
            syncMetadataDao.insertOrUpdateMetadata(
                SyncMetadataEntity(
                    recurso = "general",
                    ultimaSincronizacionExitosa = System.currentTimeMillis()
                )
            )
            return true
        }

        // Marcar estado ENVIANDO
        pendientes.forEach { op ->
            operacionPendienteDao.updateOperacion(op.copy(estado = "ENVIANDO", intentos = op.intentos + 1))
        }

        val dtoList = pendientes.map { op ->
            OperacionPendienteDto(
                uuidOperacion = op.uuidOperacion,
                entidad = op.entidad,
                idEntidadLocal = op.idEntidadLocal,
                tipoOperacion = op.tipoOperacion,
                payload = op.payload,
                fechaRegistro = op.fechaRegistro
            )
        }

        return try {
            val response = apiService.syncOperaciones(SyncRequestDto(dtoList))
            if (response.isSuccessful && response.body() != null) {
                val resultados = response.body()!!.resultados
                val resultadoMap = resultados.associateBy { it.uuidOperacion }

                pendientes.forEach { op ->
                    val res = resultadoMap[op.uuidOperacion]
                    if (res != null && res.exito) {
                        operacionPendienteDao.updateOperacion(op.copy(estado = "SINCRONIZADO", mensajeError = null))
                    } else {
                        operacionPendienteDao.updateOperacion(
                            op.copy(
                                estado = "ERROR",
                                mensajeError = res?.mensaje ?: "Error en validación del servidor"
                            )
                        )
                    }
                }

                syncMetadataDao.insertOrUpdateMetadata(
                    SyncMetadataEntity(
                        recurso = "general",
                        ultimaSincronizacionExitosa = System.currentTimeMillis()
                    )
                )
                true
            } else {
                pendientes.forEach { op ->
                    operacionPendienteDao.updateOperacion(
                        op.copy(estado = "ERROR", mensajeError = "Servidor respondió HTTP ${response.code()}")
                    )
                }
                false
            }
        } catch (e: Exception) {
            pendientes.forEach { op ->
                operacionPendienteDao.updateOperacion(
                    op.copy(estado = "PENDIENTE", mensajeError = e.localizedMessage ?: "Sin conexión con servidor")
                )
            }
            false
        }
    }

    suspend fun limpiarSincronizadas() {
        operacionPendienteDao.clearSincronizadas()
    }
}
