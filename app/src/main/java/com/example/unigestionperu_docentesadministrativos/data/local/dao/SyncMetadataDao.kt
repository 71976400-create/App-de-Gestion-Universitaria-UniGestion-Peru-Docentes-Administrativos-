package com.example.unigestionperu_docentesadministrativos.data.local.dao

import androidx.room.*
import com.example.unigestionperu_docentesadministrativos.data.local.entities.SyncMetadataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SyncMetadataDao {
    @Query("SELECT * FROM sync_metadata WHERE recurso = :recurso LIMIT 1")
    suspend fun getMetadata(recurso: String): SyncMetadataEntity?

    @Query("SELECT * FROM sync_metadata")
    fun observeAllMetadata(): Flow<List<SyncMetadataEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateMetadata(metadata: SyncMetadataEntity): Long
}
