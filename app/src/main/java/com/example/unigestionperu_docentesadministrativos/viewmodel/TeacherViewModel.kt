package com.example.unigestionperu_docentesadministrativos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.unigestionperu_docentesadministrativos.data.database.AppDatabase
import com.example.unigestionperu_docentesadministrativos.data.database.CursoEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

data class EstudianteConNotas(
    val matriculaId: Long,
    val estudianteId: Long,
    val nombreEstudiante: String,
    val emailEstudiante: String,
    val cursoId: Long,
    val nota1: Double,
    val nota2: Double,
    val examenFinal: Double,
    val promedio: Double
)

class TeacherViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val cursoDao = database.cursoDao()
    private val matriculaDao = database.matriculaDao()
    private val userDao = database.usuarioDao()

    private val _docenteId = MutableStateFlow<Long>(1L)

    @OptIn(ExperimentalCoroutinesApi::class)
    val cursosDocente: Flow<List<CursoEntity>> = _docenteId.flatMapLatest { id ->
        cursoDao.getCursosByDocente(id)
    }

    fun setDocenteId(id: Long) {
        _docenteId.value = id
    }

    fun getEstudiantesPorCurso(cursoId: Long): Flow<List<EstudianteConNotas>> {
        return matriculaDao.getMatriculasByCurso(cursoId).map { matriculas ->
            matriculas.map { mat ->
                val estudiante = userDao.getUsuarioById(mat.estudianteId)
                EstudianteConNotas(
                    matriculaId = mat.id,
                    estudianteId = mat.estudianteId,
                    nombreEstudiante = estudiante?.nombre ?: "Desconocido",
                    emailEstudiante = estudiante?.email ?: "",
                    cursoId = mat.cursoId,
                    nota1 = mat.nota1,
                    nota2 = mat.nota2,
                    examenFinal = mat.examenFinal,
                    promedio = mat.promedio
                )
            }
        }
    }

    fun actualizarNotas(matriculaId: Long, nota1: Double, nota2: Double, examenFinal: Double, onComplete: () -> Unit) {
        viewModelScope.launch {
            val n1 = nota1.coerceIn(0.0, 20.0)
            val n2 = nota2.coerceIn(0.0, 20.0)
            val ef = examenFinal.coerceIn(0.0, 20.0)
            val promedio = ((n1 + n2 + ef) / 3.0 * 10.0).toInt() / 10.0

            matriculaDao.updateNotas(matriculaId, n1, n2, ef, promedio)
            onComplete()
        }
    }
}
