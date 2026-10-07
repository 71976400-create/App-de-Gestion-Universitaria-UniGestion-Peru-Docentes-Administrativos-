package com.example.unigestionperu_docentesadministrativos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.unigestionperu_docentesadministrativos.UniGestionApplication
import com.example.unigestionperu_docentesadministrativos.data.local.entities.OperacionPendienteEntity
import com.example.unigestionperu_docentesadministrativos.data.local.entities.SyncMetadataEntity
import com.example.unigestionperu_docentesadministrativos.data.repository.SyncRepository
import com.example.unigestionperu_docentesadministrativos.util.ConnectivityObserver
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SyncUiState(
    val operacionesPendientes: List<OperacionPendienteEntity> = emptyList(),
    val totalPendientes: Int = 0,
    val metadataList: List<SyncMetadataEntity> = emptyList(),
    val isConnected: Boolean = true,
    val isSyncing: Boolean = false,
    val mensageEstado: String? = null
)

class SyncViewModel(application: Application) : AndroidViewModel(application) {
    private val appContainer = (application as UniGestionApplication).appContainer
    private val syncRepository: SyncRepository = appContainer.syncRepository
    private val connectivityObserver: ConnectivityObserver = appContainer.connectivityObserver

    val isConnected: StateFlow<Boolean> = connectivityObserver.isConnected
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val operacionesPendientes: StateFlow<List<OperacionPendienteEntity>> =
        syncRepository.observeOperacionesPendientes()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalPendientes: StateFlow<Int> = syncRepository.observeCountPendientes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val metadataList: StateFlow<List<SyncMetadataEntity>> = syncRepository.observeMetadata()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing = _isSyncing.asStateFlow()

    private val _mensajeEstado = MutableStateFlow<String?>(null)
    val mensajeEstado = _mensajeEstado.asStateFlow()

    fun sincronizarAhora() {
        viewModelScope.launch {
            _isSyncing.value = true
            _mensajeEstado.value = "Sincronizando operaciones con el servidor API REST..."
            val exito = syncRepository.sincronizar()
            _isSyncing.value = false
            if (exito) {
                _mensajeEstado.value = "Sincronización completada con éxito."
            } else {
                _mensajeEstado.value = "Sincronización finalizada con algunos errores o servidor no disponible."
            }
        }
    }

    fun limpiarSincronizadas() {
        viewModelScope.launch {
            syncRepository.limpiarSincronizadas()
        }
    }
}
