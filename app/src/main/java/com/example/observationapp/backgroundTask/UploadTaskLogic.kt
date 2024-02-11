package com.example.observationapp.backgroundTask

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.observationapp.di.DataStoreRepoInterface
import com.example.observationapp.di.MainNotificationCompactBuilder
import com.example.observationapp.di.ProgressNotificationCompactBuilder
import com.example.observationapp.models.ObservationHistory
import com.example.observationapp.observation.observation_history.datalayer.ObservationHistoryUseCase
import com.example.observationapp.repository.database.ObservationHistoryDBRepository
import com.example.observationapp.util.APIResult
import com.example.observationapp.util.CommonConstant
import com.example.observationapp.util.Utility.customisedImageList
import com.example.observationapp.util.Utility.getTodayDateAndTime
import com.example.observationapp.util.Utility.prepareFilePart
import com.google.gson.JsonObject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class UploadTaskLogic @Inject constructor(
    @ApplicationContext val context: Context,
    private val notificationManager: NotificationManagerCompat,
    @MainNotificationCompactBuilder
    private val notificationBuilder: NotificationCompat.Builder,
    @ProgressNotificationCompactBuilder
    private val notificationBuilderProgress: NotificationCompat.Builder,
    private val observationHistoryRepo: ObservationHistoryDBRepository,
    private val observationHistoryUseCase: ObservationHistoryUseCase,
    private val dataStoreRepoInterface: DataStoreRepoInterface
) {
    var userId = ""

    init {
        CoroutineScope(Dispatchers.IO).launch {
            userId = dataStoreRepoInterface.getString(CommonConstant.USER_ID) ?: ""
        }
    }


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
    fun updateNotification(title: String, message: String = "") {
        if (checkPermissionFunction()) {
            notificationManager.notify(
                15,
                notificationBuilder
                    .setContentTitle(title)
                    .setContentText(message)
                    .setProgress(0, 0, false)
                    .build()
            )
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun showProgress(
        max: Int = 0,
        progress: Int = 0,
        isProgress: Boolean = false,
        isSuccess: Boolean = true
    ) {
        if (checkPermissionFunction()) {
            /*val max = 10
            var progress = 0*/

            coroutineScope {
                if (isProgress) {
                    notificationManager.notify(
                        15,
                        notificationBuilderProgress
                            .setContentTitle("Uploading files")
                            .setContentText("${progress}/${max}")
                            .setProgress(max, progress, false)
                            .build()

                    )
                } else {
                    notificationManager.notify(
                        15,
                        notificationBuilder
                            .setProgress(0, 0, false)
                            .build()
                    )
                }


            }

        }
    }

    suspend fun uploadImagesToServer() {
        val list = observationHistoryRepo.getOfflineObservationHistoryList()

        Log.d(TAG, "uploadImagesToServer: list: $list")

        if (userId.isNotEmpty() && list.isNotEmpty()) {

            list.forEachIndexed { index, model ->
                showProgress(list.size, index + 1, true)
                val requestBody =
                    model.temp_observation_number.toRequestBody(CommonConstant.MULTIPART.toMediaTypeOrNull())
                val value = CoroutineScope(Dispatchers.IO).async {
                    val value = observationHistoryUseCase.saveObservationImagesAPI(
                        prepareFilePart(model.observation_image),
                        requestBody,
                        userId
                    )
                    return@async value.status
                }
                when (value.await()) {
                    APIResult.Status.SUCCESS -> {
                        Log.d(
                            TAG,
                            "uploadImagesToServer: loop count, $index, tmp: ${model.temp_observation_number}, primartId: ${model.primaryObservationId}"
                        )
                        val updatedItem =
                            CoroutineScope(Dispatchers.IO).async {
                                observationHistoryRepo.updateObservationHistory(
                                    true,
                                    arrayListOf(),
                                    model.primaryObservationId
                                )
                            }
                        Log.e(TAG, "uploadImagesToServer: started Update: ")
                        val item = updatedItem.await()

                        Log.d(TAG, "uploadImagesToServer: updatedId: $item")
                    }

                    APIResult.Status.ERROR -> {
                        Log.e(TAG, "uploadImagesToServer: error")
                    }
                }
            }
            Log.d(TAG, "uploadImagesToServer: exit loop")
            showProgress()
        } else {
            Log.e(TAG, "saveObservationHistoryAPI: userID or List is empty")
        }


    }

    suspend fun uploadFormToServer() {
        val list = observationHistoryRepo.getOfflineObservationFormHistoryList()
        Log.d(TAG, "uploadFormToServer: list size: ${list.size}")

        if (userId.isNotEmpty() && list.isNotEmpty()) {
            list.forEach { model ->
                val jsonObject = generateJson(model)
                val value = CoroutineScope(Dispatchers.IO).async {
                    return@async observationHistoryUseCase.saveObservationFormFlow(
                        userId,
                        jsonObject
                    )
                }
                val response = value.await()
                when (response.status) {
                    APIResult.Status.SUCCESS -> {
                        val localData = response.data!!.result
                        Log.d(TAG, "uploadFormToServer: localData: $localData")
                        val updatedItem =
                            CoroutineScope(Dispatchers.IO).async {
                                observationHistoryRepo.updateFormObservationHistory(
                                    true,
                                    localData.observation_number,
                                    model.primaryObservationId
                                )
                            }
                        Log.e(TAG, "uploadFormToServer: started Update: ")
                        val item = updatedItem.await()

                        Log.d(TAG, "uploadFormToServer: Upload success, itemNumber: $item")
                    }

                    APIResult.Status.ERROR -> {
                        Log.e(TAG, "uploadFormToServer: ${response.message}")
                    }
                }
            }
        } else {
            Log.d(TAG, "uploadFormToServer: userID or List is empty")
        }

    }

    private fun generateJson(model: ObservationHistory): JsonObject {
        val json = JsonObject()
        json.addProperty("project_id", model.project_id)
        json.addProperty("structure_id", model.structure_id)
        json.addProperty("floors", model.floors)
        json.addProperty("tradegroup_id", model.tradegroup_id)
        json.addProperty("activity_id", model.activityOrTradeId)
        json.addProperty("temp_observation_number", model.temp_observation_number)
        json.addProperty("observation_category", model.observation_category)
        json.addProperty("observation_type", model.observation_type)
        json.addProperty("location", model.location)
        json.addProperty("description", model.description)
        json.addProperty("remark", model.remark)
        json.addProperty("reference", model.reference)
        json.addProperty("observation_severity", model.observation_severity)
        json.addProperty("site_representative", model.site_representative)
        json.addProperty("status", model.status)
        json.addProperty("closed_by", model.closed_by)
        json.addProperty("observation_date", getTodayDateAndTime())
        json.addProperty("target_date", model.target_date)
        json.add(
            "observation_image", customisedImageList(
                model.observation_image,
                model.project_id,
                model.structure_id,
                model.activityOrTradeId,
                TAG
            )
        )
        Log.d(TAG, "saveForm: $json")
        return json
    }

    companion object {
        private const val TAG = "UploadTaskLogic"
    }

}