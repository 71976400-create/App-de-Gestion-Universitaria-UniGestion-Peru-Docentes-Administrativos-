package com.example.unigestionperu_docentesadministrativos.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.unigestionperu_docentesadministrativos.UniGestionApplication
import com.example.unigestionperu_docentesadministrativos.data.local.DatabaseSeeder
import com.example.unigestionperu_docentesadministrativos.data.local.entities.UsuarioEntity
import kotlinx.coroutines.launch

data class AuthUiState(
    val usuarioLogueado: UsuarioEntity? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val appContainer = (application as UniGestionApplication).appContainer
    private val database = appContainer.database
    private val userDao = database.usuarioDao()
    private val authRepository = appContainer.authRepository

    var uiState by mutableStateOf(AuthUiState())
        private set

    init {
        viewModelScope.launch {
            ensureDemoDataSeeded()
        }
    }

    private suspend fun ensureDemoDataSeeded() {
        val admin = userDao.getUsuarioByEmail("admin@unigestion.edu.pe")
        if (admin == null) {
            DatabaseSeeder.seed(database)
        }
    }

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            val cleanEmail = email.trim()
            val cleanPassword = password.trim()

            var user = authRepository.login(cleanEmail, cleanPassword)
            if (user == null) {
                ensureDemoDataSeeded()
                user = authRepository.login(cleanEmail, cleanPassword)
            }

            if (user != null && user.password == cleanPassword) {
                uiState = uiState.copy(usuarioLogueado = user, isLoading = false)
                onResult(true, user.rol)
            } else {
                val error = "Correo o contraseña incorrectos. Utilice los accesos rápidos o credenciales institucionales."
                uiState = uiState.copy(errorMessage = error, isLoading = false)
                onResult(false, null)
            }
        }
    }

    fun logout() {
        uiState = AuthUiState()
    }
}
