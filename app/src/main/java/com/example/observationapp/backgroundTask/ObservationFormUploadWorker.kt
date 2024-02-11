package com.example.observationapp.backgroundTask

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlin.coroutines.cancellation.CancellationException

@HiltWorker
class ObservationFormUploadWorker @AssistedInject constructor(
    @Assisted appContext: Context, @Assisted params: WorkerParameters,
    private val uploadTaskLogic: UploadTaskLogic
) : CoroutineWorker(appContext, params) {
    companion object {
        private const val TAG = "ObservationFormUpload"
    }

    override suspend fun doWork(): Result {

        try {
            uploadTaskLogic.uploadFormToServer()
            uploadTaskLogic.uploadImagesToServer()
        } catch (e: CancellationException) {
            Log.e(TAG, "doWork: task cancelled", e)
        }

        if (isStopped) {
            Log.d(TAG, "doWork: something went wrong!")
            uploadTaskLogic.updateNotification(
                "Image upload is cancelled!",
                "Please check the internet is connected or not!"
            )
            return Result.retry()
        }


        Log.d(TAG, "doWork: Worked thread")

        return Result.success()
    }


}