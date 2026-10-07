package com.example.unigestionperu_docentesadministrativos

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.unigestionperu_docentesadministrativos.di.AppContainer
import com.example.unigestionperu_docentesadministrativos.worker.SyncWorker
import java.util.concurrent.TimeUnit

class UniGestionApplication : Application() {
    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
        setupWorkManagerSync()
    }

    private fun setupWorkManagerSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncWorkRequest = PeriodicWorkRequestBuilder<SyncWorker>(15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "UniGestionSyncWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            syncWorkRequest
        )
    }
}
