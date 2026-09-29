package com.example.unigestionperu_docentesadministrativos.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.unigestionperu_docentesadministrativos.data.database.AppDatabase
import com.example.unigestionperu_docentesadministrativos.data.database.UsuarioEntity
import kotlinx.coroutines.launch

data class AuthUiState(
    val usuarioLogueado: UsuarioEntity? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val userDao = AppDatabase.getDatabase(application).usuarioDao()

    var uiState by mutableStateOf(AuthUiState())
        private set

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            val user = userDao.getUsuarioByEmail(email.trim())
            if (user != null && user.password == password) {
                uiState = uiState.copy(usuarioLogueado = user, isLoading = false)
                onResult(true, user.rol)
            } else {
                val error = "Correo o contraseña incorrectos"
                uiState = uiState.copy(errorMessage = error, isLoading = false)
                onResult(false, null)
            }
        }
    }

    fun logout() {
        uiState = AuthUiState()
    }
}
