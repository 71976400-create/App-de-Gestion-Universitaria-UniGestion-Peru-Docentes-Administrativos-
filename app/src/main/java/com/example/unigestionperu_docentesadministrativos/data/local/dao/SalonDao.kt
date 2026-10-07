package com.example.unigestionperu_docentesadministrativos.data.local.dao

import androidx.room.*
import com.example.unigestionperu_docentesadministrativos.data.local.entities.SalonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SalonDao {
    @Query("SELECT * FROM salones ORDER BY id ASC")
    fun getAllSalones(): Flow<List<SalonEntity>>

    @Query("SELECT * FROM salones WHERE id = :id")
    suspend fun getSalonById(id: Long): SalonEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSalon(salon: SalonEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSalones(salones: List<SalonEntity>): List<Long>

    @Update
    suspend fun updateSalon(salon: SalonEntity): Int

    @Delete
    suspend fun deleteSalon(salon: SalonEntity): Int

    @Query("SELECT COUNT(*) FROM salones")
    suspend fun countSalones(): Int
}
