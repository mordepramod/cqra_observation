package com.example.observationapp.backgroundTask

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.observationapp.di.MainNotificationCompactBuilder
import com.example.observationapp.di.ProgressNotificationCompactBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import javax.inject.Inject

class UploadTaskLogic @Inject constructor(
    @ApplicationContext val context: Context,
    private val notificationManager: NotificationManagerCompat,
    @MainNotificationCompactBuilder
    private val notificationBuilder: NotificationCompat.Builder,
    @ProgressNotificationCompactBuilder
    private val notificationBuilderProgress: NotificationCompat.Builder
) {

    /*@Inject
    @ProgressNotificationCompactBuilder
    lateinit var notificationBuilderProgress: NotificationCompat.Builder*/

    @SuppressLint("MissingPermission")
    fun showNotification() {
        if (checkPermissionFunction()) {
            notificationManager.notify(15, notificationBuilder.build())
        }
    }

    private fun checkPermissionFunction(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.e(TAG, "showNotification: Notification permission denied")
            return false
        }
        return true
    }

    @SuppressLint("MissingPermission")
    fun cancelNotification() {
        if (checkPermissionFunction()) {
            notificationManager.cancel(15)
        }
    }

    @SuppressLint("MissingPermission")
    fun updateNotification() {
        if (checkPermissionFunction()) {
            notificationManager.notify(
                15,
                notificationBuilder.setContentTitle("New Upload form").build()
            )
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun showProgress() {
        if (checkPermissionFunction()) {
            val max = 10
            var progress = 0
            coroutineScope {
                while (progress != max) {
                    delay(2000)
                    progress += 1
                    notificationManager.notify(
                        15,
                        notificationBuilderProgress
                            .setContentTitle("Uploading files")
                            .setContentText("${progress}/${max}")
                            .setProgress(max, progress, false)
                            .build()

                    )
                }
                notificationManager.notify(
                    15,
                    notificationBuilder
                        .setProgress(0, 0, false)
                        .build()
                )
            }

        }
    }

    companion object {
        private const val TAG = "UploadTaskLogic"
    }

}