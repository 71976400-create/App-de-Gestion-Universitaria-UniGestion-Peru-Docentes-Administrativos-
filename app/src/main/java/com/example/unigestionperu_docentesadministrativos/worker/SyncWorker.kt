package com.example.unigestionperu_docentesadministrativos.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.unigestionperu_docentesadministrativos.UniGestionApplication

class SyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val appContainer = (applicationContext as UniGestionApplication).appContainer
        val syncRepository = appContainer.syncRepository

        return try {
            val exito = syncRepository.sincronizar()
            if (exito) Result.success() else Result.retry()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
