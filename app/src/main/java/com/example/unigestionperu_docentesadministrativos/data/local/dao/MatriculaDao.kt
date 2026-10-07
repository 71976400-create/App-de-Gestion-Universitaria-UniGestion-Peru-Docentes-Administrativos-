package com.example.unigestionperu_docentesadministrativos.data.local.dao

import androidx.room.*
import com.example.unigestionperu_docentesadministrativos.data.local.entities.MatriculaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MatriculaDao {
    @Query("SELECT * FROM matriculas WHERE cursoId = :cursoId")
    fun getMatriculasByCurso(cursoId: Long): Flow<List<MatriculaEntity>>

    @Query("SELECT * FROM matriculas WHERE estudianteId = :estudianteId")
    fun getMatriculasByEstudiante(estudianteId: Long): Flow<List<MatriculaEntity>>

    @Query("SELECT * FROM matriculas WHERE id = :id")
    suspend fun getMatriculaById(id: Long): MatriculaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatricula(matricula: MatriculaEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatriculas(matriculas: List<MatriculaEntity>): List<Long>

    @Update
    suspend fun updateMatricula(matricula: MatriculaEntity): Int

    @Query("UPDATE matriculas SET nota1 = :nota1, nota2 = :nota2, examenFinal = :examenFinal, promedio = :promedio WHERE id = :matriculaId")
    suspend fun updateNotas(matriculaId: Long, nota1: Double, nota2: Double, examenFinal: Double, promedio: Double): Int

    @Delete
    suspend fun deleteMatricula(matricula: MatriculaEntity): Int

    @Query("SELECT COUNT(*) FROM matriculas")
    suspend fun countMatriculas(): Int
}
