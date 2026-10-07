package com.example.unigestionperu_docentesadministrativos.data.repository

import com.example.unigestionperu_docentesadministrativos.data.local.dao.CursoDao
import com.example.unigestionperu_docentesadministrativos.data.local.dao.MatriculaDao
import com.example.unigestionperu_docentesadministrativos.data.local.dao.OperacionPendienteDao
import com.example.unigestionperu_docentesadministrativos.data.local.entities.CursoEntity
import com.example.unigestionperu_docentesadministrativos.data.local.entities.MatriculaEntity
import com.example.unigestionperu_docentesadministrativos.data.local.entities.OperacionPendienteEntity
import com.example.unigestionperu_docentesadministrativos.data.remote.api.ApiService
import com.example.unigestionperu_docentesadministrativos.data.mapper.toDto
import com.example.unigestionperu_docentesadministrativos.data.mapper.toEntity
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class CursoRepository(
    private val cursoDao: CursoDao,
    private val matriculaDao: MatriculaDao,
    private val operacionPendienteDao: OperacionPendienteDao,
    private val apiService: ApiService,
    private val gson: Gson = Gson()
) {
    fun getAllCursos(): Flow<List<CursoEntity>> = cursoDao.getAllCursos()

    fun getCursosByDocente(docenteId: Long): Flow<List<CursoEntity>> = cursoDao.getCursosByDocente(docenteId)

    suspend fun getCursoById(id: Long): CursoEntity? = cursoDao.getCursoById(id)

    fun getMatriculasByCurso(cursoId: Long): Flow<List<MatriculaEntity>> = matriculaDao.getMatriculasByCurso(cursoId)

    // RF07 & RF10: Registrar/editar nota validando que esté entre 0 y 20
    suspend fun actualizarNotasMatricula(matricula: MatriculaEntity): Boolean {
        if (matricula.nota1 !in 0.0..20.0 || matricula.nota2 !in 0.0..20.0 || matricula.examenFinal !in 0.0..20.0) {
            return false
        }
        val promedioCalculado = (matricula.nota1 + matricula.nota2 + (matricula.examenFinal * 2)) / 4.0
        val matriculaActualizada = matricula.copy(promedio = Math.round(promedioCalculado * 10.0) / 10.0)

        // 1. Guardar localmente en Room
        matriculaDao.updateMatricula(matriculaActualizada)

        // 2. Transacción de operación pendiente con UUID para sincronización
        val uuid = UUID.randomUUID().toString()
        operacionPendienteDao.insertOperacion(
            OperacionPendienteEntity(
                uuidOperacion = uuid,
                entidad = "matricula",
                idEntidadLocal = matriculaActualizada.id,
                tipoOperacion = "UPDATE",
                payload = gson.toJson(matriculaActualizada.toDto())
            )
        )
        return true
    }

    // RF09: Crear o editar curso
    suspend fun agregarCurso(curso: CursoEntity): Long {
        val id = cursoDao.insertCurso(curso)
        val cursoGuardado = curso.copy(id = id)

        val uuid = UUID.randomUUID().toString()
        operacionPendienteDao.insertOperacion(
            OperacionPendienteEntity(
                uuidOperacion = uuid,
                entidad = "curso",
                idEntidadLocal = id,
                tipoOperacion = "INSERT",
                payload = gson.toJson(cursoGuardado.toDto())
            )
        )
        return id
    }

    suspend fun actualizarCurso(curso: CursoEntity) {
        cursoDao.updateCurso(curso)

        val uuid = UUID.randomUUID().toString()
        operacionPendienteDao.insertOperacion(
            OperacionPendienteEntity(
                uuidOperacion = uuid,
                entidad = "curso",
                idEntidadLocal = curso.id,
                tipoOperacion = "UPDATE",
                payload = gson.toJson(curso.toDto())
            )
        )
    }

    suspend fun eliminarCurso(curso: CursoEntity) {
        cursoDao.deleteCurso(curso)

        val uuid = UUID.randomUUID().toString()
        operacionPendienteDao.insertOperacion(
            OperacionPendienteEntity(
                uuidOperacion = uuid,
                entidad = "curso",
                idEntidadLocal = curso.id,
                tipoOperacion = "DELETE",
                payload = gson.toJson(curso.toDto())
            )
        )
    }

    // RF11: Descargar cursos remotos y actualizar Room
    suspend fun sincronizarCursosDesdeApi(): Boolean {
        return try {
            val response = apiService.getCursos()
            if (response.isSuccessful && response.body() != null) {
                val cursosRemotos = response.body()!!.map { it.toEntity() }
                cursoDao.insertCursos(cursosRemotos)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}
