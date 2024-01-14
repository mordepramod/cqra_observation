package com.example.observationapp.dashboard.domainlayer

import android.util.Log
import com.example.observationapp.models.ProjectModel
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
    suspend fun getProjectListAPI(userId: String): APIResult<ProjectModel> {
        return try {
            val projectModelResponse = apiService.getProjectListAPI(userId)
            if (projectModelResponse.isSuccessful && projectModelResponse.code() == HttpURLConnection.HTTP_OK) {
                APIResult.success(projectModelResponse.body()!!)
            } else {
                APIResult.error(projectModelResponse.message(), null, null)
            }
        } catch (exception: Exception) {
            Log.e(TAG, "getProjectListAPI: ", exception)
            val errorMsg = exception.message ?: "error in getProjectListAPI $TAG"
            APIResult.error(errorMsg, null, exception)
        }
    }
}