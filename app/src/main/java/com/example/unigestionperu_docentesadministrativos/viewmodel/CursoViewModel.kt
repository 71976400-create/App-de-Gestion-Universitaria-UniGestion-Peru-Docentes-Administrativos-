package com.example.unigestionperu_docentesadministrativos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.unigestionperu_docentesadministrativos.UniGestionApplication
import com.example.unigestionperu_docentesadministrativos.data.local.entities.CursoEntity
import com.example.unigestionperu_docentesadministrativos.data.local.entities.MatriculaEntity
import com.example.unigestionperu_docentesadministrativos.data.repository.CursoRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CursoUiState(
    val cursos: List<CursoEntity> = emptyList(),
    val filtroTexto: String = "",
    val isLoading: Boolean = false,
    val mensajeError: String? = null
)

class CursoViewModel(application: Application) : AndroidViewModel(application) {
    private val appContainer = (application as UniGestionApplication).appContainer
    private val cursoRepository: CursoRepository = appContainer.cursoRepository

    private val _filtroTexto = MutableStateFlow("")
    val filtroTexto = _filtroTexto.asStateFlow()

    val cursos: StateFlow<List<CursoEntity>> = cursoRepository.getAllCursos()
        .combine(_filtroTexto) { list, query ->
            if (query.isBlank()) list
            else list.filter {
                it.nombre.contains(query, ignoreCase = true) ||
                it.codigo.contains(query, ignoreCase = true) ||
                it.facultad.contains(query, ignoreCase = true)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setFiltro(query: String) {
        _filtroTexto.value = query
    }

    fun getMatriculasPorCurso(cursoId: Long): Flow<List<MatriculaEntity>> {
        return cursoRepository.getMatriculasByCurso(cursoId)
    }

    fun actualizarNotasMatricula(matricula: MatriculaEntity, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            if (matricula.nota1 !in 0.0..20.0 || matricula.nota2 !in 0.0..20.0 || matricula.examenFinal !in 0.0..20.0) {
                onResult(false, "La nota registrada debe estar estrictamente entre 0 y 20 (RF10).")
                return@launch
            }
            val exito = cursoRepository.actualizarNotasMatricula(matricula)
            if (exito) {
                onResult(true, "Notas actualizadas localmente. Sincronización registrada en cola.")
            } else {
                onResult(false, "Error al actualizar notas.")
            }
        }
    }

    fun sincronizarDesdeApi() {
        viewModelScope.launch {
            cursoRepository.sincronizarCursosDesdeApi()
        }
    }
}
