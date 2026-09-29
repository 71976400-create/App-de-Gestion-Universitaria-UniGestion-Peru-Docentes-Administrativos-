package com.example.unigestionperu_docentesadministrativos.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun getUsuarioByEmail(email: String): UsuarioEntity?

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun getUsuarioById(id: Long): UsuarioEntity?

    @Query("SELECT * FROM usuarios")
    fun getAllUsuarios(): Flow<List<UsuarioEntity>>

    @Query("SELECT * FROM usuarios WHERE rol = :rol")
    fun getUsuariosByRol(rol: String): Flow<List<UsuarioEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsuario(usuario: UsuarioEntity): Long
}

@Dao
interface CursoDao {
    @Query("SELECT * FROM cursos WHERE docenteId = :docenteId")
    fun getCursosByDocente(docenteId: Long): Flow<List<CursoEntity>>

    @Query("SELECT * FROM cursos")
    fun getAllCursos(): Flow<List<CursoEntity>>

    @Query("SELECT * FROM cursos WHERE id = :id LIMIT 1")
    suspend fun getCursoById(id: Long): CursoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurso(curso: CursoEntity): Long

    @Update
    suspend fun updateCurso(curso: CursoEntity): Int
}

@Dao
interface MatriculaDao {
    @Query("SELECT * FROM matriculas WHERE cursoId = :cursoId")
    fun getMatriculasByCurso(cursoId: Long): Flow<List<MatriculaEntity>>

    @Query("UPDATE matriculas SET nota1 = :nota1, nota2 = :nota2, examenFinal = :examenFinal, promedio = :promedio WHERE id = :matriculaId")
    suspend fun updateNotas(matriculaId: Long, nota1: Double, nota2: Double, examenFinal: Double, promedio: Double): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatricula(matricula: MatriculaEntity): Long
}
