package com.example.unigestionperu_docentesadministrativos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.unigestionperu_docentesadministrativos.UniGestionApplication
import com.example.unigestionperu_docentesadministrativos.data.local.entities.CursoEntity
import com.example.unigestionperu_docentesadministrativos.data.local.entities.SalonEntity
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
    private val appContainer = (application as UniGestionApplication).appContainer
    private val database = appContainer.database
    private val cursoDao = database.cursoDao()
    private val matriculaDao = database.matriculaDao()
    private val userDao = database.usuarioDao()
    private val salonDao = database.salonDao()
    private val cursoRepository = appContainer.cursoRepository
    private val salonRepository = appContainer.salonRepository

    private val _docenteId = MutableStateFlow<Long>(1L)

    @OptIn(ExperimentalCoroutinesApi::class)
    val cursosDocente: Flow<List<CursoEntity>> = _docenteId.flatMapLatest { id ->
        cursoDao.getCursosByDocente(id)
    }

    val allSalones: Flow<List<SalonEntity>> = salonDao.getAllSalones()

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
            val promedio = ((n1 + n2 + (ef * 2)) / 4.0 * 10.0).toInt() / 10.0

            val matricula = matriculaDao.getMatriculaById(matriculaId)
            if (matricula != null) {
                val matriculaActualizada = matricula.copy(
                    nota1 = n1,
                    nota2 = n2,
                    examenFinal = ef,
                    promedio = promedio
                )
                cursoRepository.actualizarNotasMatricula(matriculaActualizada)
            }
            onComplete()
        }
    }

    // Gestión de Salones para Docentes
    fun agregarSalon(
        codigo: String,
        edificio: String,
        capacidad: Int,
        ocupados: Int,
        tipo: String,
        docenteAsignado: String,
        horario: String,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch {
            salonRepository.agregarSalon(
                SalonEntity(
                    codigo = codigo,
                    edificio = edificio,
                    capacidad = capacidad,
                    ocupados = ocupados,
                    tipo = tipo,
                    docenteAsignado = docenteAsignado,
                    horario = horario
                )
            )
            onComplete()
        }
    }

    fun actualizarSalon(salon: SalonEntity, onComplete: () -> Unit) {
        viewModelScope.launch {
            salonRepository.actualizarSalon(salon)
            onComplete()
        }
    }

        fun eliminarSalon(salon: SalonEntity, onComplete: () -> Unit) {
        viewModelScope.launch {
            salonRepository.eliminarSalon(salon)
            onComplete()
        }
    }
}
