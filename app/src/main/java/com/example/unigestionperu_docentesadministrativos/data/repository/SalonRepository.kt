package com.example.unigestionperu_docentesadministrativos.data.repository

import com.example.unigestionperu_docentesadministrativos.data.local.dao.OperacionPendienteDao
import com.example.unigestionperu_docentesadministrativos.data.local.dao.SalonDao
import com.example.unigestionperu_docentesadministrativos.data.local.entities.OperacionPendienteEntity
import com.example.unigestionperu_docentesadministrativos.data.local.entities.SalonEntity
import com.example.unigestionperu_docentesadministrativos.data.remote.api.ApiService
import com.example.unigestionperu_docentesadministrativos.data.mapper.toDto
import com.example.unigestionperu_docentesadministrativos.data.mapper.toEntity
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class SalonRepository(
    private val salonDao: SalonDao,
    private val operacionPendienteDao: OperacionPendienteDao,
    private val apiService: ApiService,
    private val gson: Gson = Gson()
) {
    fun getAllSalones(): Flow<List<SalonEntity>> = salonDao.getAllSalones()

    suspend fun getSalonById(id: Long): SalonEntity? = salonDao.getSalonById(id)

    suspend fun agregarSalon(salon: SalonEntity): Long {
        val id = salonDao.insertSalon(salon)
        val salonGuardado = salon.copy(id = id)

        val uuid = UUID.randomUUID().toString()
        operacionPendienteDao.insertOperacion(
            OperacionPendienteEntity(
                uuidOperacion = uuid,
                entidad = "salon",
                idEntidadLocal = id,
                tipoOperacion = "INSERT",
                payload = gson.toJson(salonGuardado.toDto())
            )
        )
        return id
    }

    suspend fun actualizarSalon(salon: SalonEntity) {
        salonDao.updateSalon(salon)

        val uuid = UUID.randomUUID().toString()
        operacionPendienteDao.insertOperacion(
            OperacionPendienteEntity(
                uuidOperacion = uuid,
                entidad = "salon",
                idEntidadLocal = salon.id,
                tipoOperacion = "UPDATE",
                payload = gson.toJson(salon.toDto())
            )
        )
    }

    suspend fun eliminarSalon(salon: SalonEntity) {
        salonDao.deleteSalon(salon)

        val uuid = UUID.randomUUID().toString()
        operacionPendienteDao.insertOperacion(
            OperacionPendienteEntity(
                uuidOperacion = uuid,
                entidad = "salon",
                idEntidadLocal = salon.id,
                tipoOperacion = "DELETE",
                payload = gson.toJson(salon.toDto())
            )
        )
    }

    suspend fun sincronizarSalonesDesdeApi(): Boolean {
        return try {
            val response = apiService.getSalones()
            if (response.isSuccessful && response.body() != null) {
                val salonesRemotos = response.body()!!.map { it.toEntity() }
                salonDao.insertSalones(salonesRemotos)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}
