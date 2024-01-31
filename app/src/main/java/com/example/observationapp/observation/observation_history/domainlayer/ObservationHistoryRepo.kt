package com.example.observationapp.observation.observation_history.domainlayer

import android.util.Log
import com.example.observationapp.models.ImageSaveResponseModel
import com.example.observationapp.models.ObservationHistoryModel
import com.example.observationapp.models.SingleObservationHistoryModel
import com.example.observationapp.repository.server.ApiHelper
import com.example.observationapp.util.APIResult
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.net.HttpURLConnection
import javax.inject.Inject

class ObservationHistoryRepo @Inject constructor() {
    companion object {
        private const val TAG = "ObservationHistoryRepo"
    }

    @Inject
    lateinit var apiService: ApiHelper
    suspend fun getObservationHistoryAPI(userId: String): APIResult<ObservationHistoryModel> {
        return try {
            val projectModelResponse = apiService.getObservationHistoryAPI(userId)
            if (projectModelResponse.isSuccessful && projectModelResponse.code() == HttpURLConnection.HTTP_OK) {
                APIResult.success(projectModelResponse.body()!!)
            } else {
                APIResult.error(projectModelResponse.message(), null, null)
            }
        } catch (exception: Exception) {
            Log.e(TAG, "getObservationHistoryAPI: ", exception)
            val errorMsg = exception.message ?: "error in getObservationHistoryAPI $TAG"
            APIResult.error(errorMsg, null, exception)
        }
    }

    suspend fun saveObservationFormAPI(
        userId: String,
        json: JsonObject
    ): APIResult<SingleObservationHistoryModel> {
        return try {
            val observationHistoryModelResponse = apiService.saveObservationFormAPI(userId, json)
            if (observationHistoryModelResponse.isSuccessful && observationHistoryModelResponse.code() == HttpURLConnection.HTTP_OK) {
                APIResult.success(observationHistoryModelResponse.body()!!)
            } else {
                APIResult.error(observationHistoryModelResponse.message(), null, null)
            }
        } catch (exception: Exception) {
            Log.e(TAG, "saveObservationFormAPI: ", exception)
            val errorMsg = exception.message ?: "error in saveObservationFormAPI $TAG"
            APIResult.error(errorMsg, null, exception)
        }
    }

    suspend fun saveObservationImagesAPI(
        surveyImage: List<MultipartBody.Part>,
        tempObsId: RequestBody,
        userId: String
    ): APIResult<ImageSaveResponseModel> {
        return try {
            val imageSaveResponseModelResponse =
                apiService.saveImagesAPI(surveyImage, tempObsId, userId)
            if (imageSaveResponseModelResponse.isSuccessful && imageSaveResponseModelResponse.code() == HttpURLConnection.HTTP_OK) {
                APIResult.success(imageSaveResponseModelResponse.body()!!)
            } else {
                APIResult.error(imageSaveResponseModelResponse.message(), null, null)
            }
        } catch (exception: Exception) {
            Log.e(TAG, "saveObservationImagesAPI: ", exception)
            val errorMsg = exception.message ?: "error in saveObservationImagesAPI $TAG"
            APIResult.error(errorMsg, null, exception)
        }
    }
}