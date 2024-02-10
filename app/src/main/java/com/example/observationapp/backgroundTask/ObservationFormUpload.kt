package com.example.observationapp.backgroundTask

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay

@HiltWorker
class ObservationFormUpload @AssistedInject constructor(
    @Assisted appContext: Context, @Assisted params: WorkerParameters,
    private val uploadTaskLogic: UploadTaskLogic
) : CoroutineWorker(appContext, params) {
    companion object {
        private const val TAG = "ObservationFormUpload"
    }

    override suspend fun doWork(): Result {

        uploadTaskLogic.showNotification()

        delay(2000)

        uploadTaskLogic.updateNotification()

        delay(2000)
        uploadTaskLogic.showProgress()



        Log.d(TAG, "doWork: Worked thread")

        return Result.success()
    }


}