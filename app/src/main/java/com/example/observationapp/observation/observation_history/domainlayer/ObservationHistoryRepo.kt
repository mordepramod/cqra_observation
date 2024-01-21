package com.example.observationapp.observation.observation_history.domainlayer

import android.util.Log
import com.example.observationapp.models.ObservationHistoryModel
import com.example.observationapp.repository.server.ApiHelper
import com.example.observationapp.util.APIResult
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
}