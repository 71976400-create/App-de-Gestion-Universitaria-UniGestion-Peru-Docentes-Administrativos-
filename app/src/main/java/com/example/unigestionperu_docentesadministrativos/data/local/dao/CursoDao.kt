package com.example.unigestionperu_docentesadministrativos.data.local.dao

import androidx.room.*
import com.example.unigestionperu_docentesadministrativos.data.local.entities.CursoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CursoDao {
    @Query("SELECT * FROM cursos")
    fun getAllCursos(): Flow<List<CursoEntity>>

    @Query("SELECT * FROM cursos WHERE docenteId = :docenteId")
    fun getCursosByDocente(docenteId: Long): Flow<List<CursoEntity>>

    @Query("SELECT * FROM cursos WHERE id = :id")
    suspend fun getCursoById(id: Long): CursoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurso(curso: CursoEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCursos(cursos: List<CursoEntity>): List<Long>

    @Update
    suspend fun updateCurso(curso: CursoEntity): Int

    @Delete
    suspend fun deleteCurso(curso: CursoEntity): Int

    @Query("SELECT COUNT(*) FROM cursos")
    suspend fun countCursos(): Int
}
