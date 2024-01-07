package com.example.observationapp.dashboard.domainlayer

import android.util.Log
import com.example.observationapp.models.ProjectModelList
import com.example.observationapp.repository.server.ApiHelper
import com.example.observationapp.util.APIResult
import java.net.HttpURLConnection
import javax.inject.Inject

class ProjectListRepo @Inject constructor() {
    companion object {
        private const val TAG = "ProjectListRepo"
    }

    @Inject
    lateinit var apiService: ApiHelper
    suspend fun getProjectListAPI(): APIResult<ProjectModelList> {
        return try {
            val gdprResponse = apiService.getProjectListAPI()
            if (gdprResponse.isSuccessful && gdprResponse.code() == HttpURLConnection.HTTP_OK) {
                APIResult.success(gdprResponse.body()!!)
            } else {
                APIResult.error(gdprResponse.message(), null, null)
            }
        } catch (exception: Exception) {
            Log.e(TAG, "getProjectListAPI: ", exception)
            val errorMsg = exception.message ?: "error in getProjectListAPI $TAG"
            APIResult.error(errorMsg, null, exception)
        }
    }
}