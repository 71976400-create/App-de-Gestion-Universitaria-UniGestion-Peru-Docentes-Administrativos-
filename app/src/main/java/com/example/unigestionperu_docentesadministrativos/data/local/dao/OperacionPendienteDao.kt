package com.example.unigestionperu_docentesadministrativos.data.local.dao

import androidx.room.*
import com.example.unigestionperu_docentesadministrativos.data.local.entities.OperacionPendienteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OperacionPendienteDao {
    @Query("SELECT * FROM operaciones_pendientes WHERE estado = 'PENDIENTE' ORDER BY fechaRegistro ASC")
    suspend fun getOperacionesPendientes(): List<OperacionPendienteEntity>

    @Query("SELECT * FROM operaciones_pendientes ORDER BY fechaRegistro DESC")
    fun observeAllOperaciones(): Flow<List<OperacionPendienteEntity>>

    @Query("SELECT COUNT(*) FROM operaciones_pendientes WHERE estado = 'PENDIENTE'")
    fun observeCountPendientes(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOperacion(operacion: OperacionPendienteEntity): Long

    @Update
    suspend fun updateOperacion(operacion: OperacionPendienteEntity): Int

    @Delete
    suspend fun deleteOperacion(operacion: OperacionPendienteEntity): Int

    @Query("DELETE FROM operaciones_pendientes WHERE estado = 'SINCRONIZADO'")
    suspend fun clearSincronizadas(): Int
}
