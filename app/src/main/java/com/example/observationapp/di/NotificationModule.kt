package com.example.observationapp.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.observationapp.R
import com.example.observationapp.backgroundTask.UploadTaskLogic
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    private const val channelId = "OBSERVATION_CHANNEL_UPLOAD_IMAGES_Done"
    private const val channelProgress = "OBSERVATION_CHANNEL_UPLOAD_IMAGES_Progress"

    @Singleton
    @Provides
    @MainNotificationCompactBuilder
    fun provideNotificationBuilder(@ApplicationContext context: Context): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, channelId)
            .setContentTitle("Upload Form Completed!")
            .setContentText("Images Uploaded to server, You can clear the notification!")
            .setSmallIcon(R.drawable.ic_check)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    }

    @Singleton
    @Provides
    @ProgressNotificationCompactBuilder
    fun provideNotificationBuilderForProgress(@ApplicationContext context: Context): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, channelProgress)
            .setContentTitle("Uploading Form")
            .setContentText("Uploading form from notification")
            .setSmallIcon(R.drawable.ic_info)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
    }

    @Singleton
    @Provides
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManagerCompat {
        val notificationManager = NotificationManagerCompat.from(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Upload Image and form",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val channelProgress = NotificationChannel(
                channelProgress,
                "Upload Image and form Progress",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
            notificationManager.createNotificationChannel(channelProgress)
        }

        return notificationManager
    }

    @Singleton
    @Provides
    fun provideUploadTaskLogic(
        @ApplicationContext context: Context,
        notificationManager: NotificationManagerCompat,
        @MainNotificationCompactBuilder
        notificationBuilder: NotificationCompat.Builder,
        @ProgressNotificationCompactBuilder
        notificationBuilderProgress: NotificationCompat.Builder
    ): UploadTaskLogic {
        return UploadTaskLogic(
            context,
            notificationManager,
            notificationBuilder,
            notificationBuilderProgress
        )
    }
}


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MainNotificationCompactBuilder

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ProgressNotificationCompactBuilder
