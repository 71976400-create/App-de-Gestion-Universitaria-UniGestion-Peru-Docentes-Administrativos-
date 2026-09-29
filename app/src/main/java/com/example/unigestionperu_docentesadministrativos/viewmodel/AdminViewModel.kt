package com.example.unigestionperu_docentesadministrativos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.unigestionperu_docentesadministrativos.data.database.AppDatabase
import com.example.unigestionperu_docentesadministrativos.data.database.CursoEntity
import com.example.unigestionperu_docentesadministrativos.data.database.UsuarioEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AdminViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val userDao = database.usuarioDao()
    private val cursoDao = database.cursoDao()

    val allUsuarios: Flow<List<UsuarioEntity>> = userDao.getAllUsuarios()
    val allCursos: Flow<List<CursoEntity>> = cursoDao.getAllCursos()
    val allDocentes: Flow<List<UsuarioEntity>> = userDao.getUsuariosByRol("Docente")

    fun agregarCurso(codigo: String, nombre: String, docenteId: Long, creditos: Int, ciclo: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            cursoDao.insertCurso(
                CursoEntity(codigo = codigo, nombre = nombre, docenteId = docenteId, creditos = creditos, ciclo = ciclo)
            )
            onComplete()
        }
    }

    fun actualizarCurso(curso: CursoEntity, onComplete: () -> Unit) {
        viewModelScope.launch {
            cursoDao.updateCurso(curso)
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
}
