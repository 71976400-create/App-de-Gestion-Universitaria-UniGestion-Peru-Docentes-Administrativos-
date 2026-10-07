package com.example.unigestionperu_docentesadministrativos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.unigestionperu_docentesadministrativos.UniGestionApplication
import com.example.unigestionperu_docentesadministrativos.data.local.entities.CursoEntity
import com.example.unigestionperu_docentesadministrativos.data.local.entities.SalonEntity
import com.example.unigestionperu_docentesadministrativos.data.local.entities.UsuarioEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AdminViewModel(application: Application) : AndroidViewModel(application) {
    private val appContainer = (application as UniGestionApplication).appContainer
    private val database = appContainer.database
    private val userDao = database.usuarioDao()
    private val cursoDao = database.cursoDao()
    private val salonDao = database.salonDao()
    private val cursoRepository = appContainer.cursoRepository
    private val salonRepository = appContainer.salonRepository

    val allUsuarios: Flow<List<UsuarioEntity>> = userDao.getAllUsuarios()
    val allCursos: Flow<List<CursoEntity>> = cursoDao.getAllCursos()
    val allSalones: Flow<List<SalonEntity>> = salonDao.getAllSalones()
    val allDocentes: Flow<List<UsuarioEntity>> = userDao.getUsuariosByRol("Docente")

    fun agregarCurso(codigo: String, nombre: String, docenteId: Long, creditos: Int, ciclo: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            cursoRepository.agregarCurso(
                CursoEntity(codigo = codigo, nombre = nombre, docenteId = docenteId, creditos = creditos, ciclo = ciclo)
            )
            onComplete()
        }
    }

    fun actualizarCurso(curso: CursoEntity, onComplete: () -> Unit) {
        viewModelScope.launch {
            cursoRepository.actualizarCurso(curso)
            onComplete()
        }
    }

    fun agregarUsuario(nombre: String, email: String, password: String, rol: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            userDao.insertUsuario(
                UsuarioEntity(nombre = nombre, email = email, password = password, rol = rol)
            )
            onComplete()
        }
    }

    // Métodos para la gestión de Salones (Aulas)
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
