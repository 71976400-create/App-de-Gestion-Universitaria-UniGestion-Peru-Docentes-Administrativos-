package com.example.unigestionperu_docentesadministrativos.di

import android.content.Context
import com.example.unigestionperu_docentesadministrativos.data.local.AppDatabase
import com.example.unigestionperu_docentesadministrativos.data.remote.RetrofitClient
import com.example.unigestionperu_docentesadministrativos.data.repository.AuthRepository
import com.example.unigestionperu_docentesadministrativos.data.repository.CursoRepository
import com.example.unigestionperu_docentesadministrativos.data.repository.SalonRepository
import com.example.unigestionperu_docentesadministrativos.data.repository.SyncRepository
import com.example.unigestionperu_docentesadministrativos.util.ConnectivityObserver

class AppContainer(private val context: Context) {
    val database: AppDatabase by lazy {
        AppDatabase.getDatabase(context)
    }

    val apiService by lazy {
        RetrofitClient.apiService
    }

    val authRepository: AuthRepository by lazy {
        AuthRepository(database.usuarioDao(), apiService)
    }

    val cursoRepository: CursoRepository by lazy {
        CursoRepository(
            database.cursoDao(),
            database.matriculaDao(),
            database.operacionPendienteDao(),
            apiService
        )
    }

    val salonRepository: SalonRepository by lazy {
        SalonRepository(
            database.salonDao(),
            database.operacionPendienteDao(),
            apiService
        )
    }

    val syncRepository: SyncRepository by lazy {
        SyncRepository(
            database.operacionPendienteDao(),
            database.syncMetadataDao(),
            apiService
        )
    }

    val connectivityObserver: ConnectivityObserver by lazy {
        ConnectivityObserver(context)
    }
}
