package com.example.observationapp.repository.server

import com.example.observationapp.models.LoginResponseModel
import com.example.observationapp.models.ObservationHistoryModel
import com.example.observationapp.models.ProjectModel
import com.google.gson.JsonObject
import retrofit2.Response

interface ApiHelper {
    suspend fun getObservationHistoryAPI(projectId: String): Response<ObservationHistoryModel>
    suspend fun getProjectListAPI(userId: String): Response<ProjectModel>
    suspend fun getLoginAPI(jsonObject: JsonObject): Response<LoginResponseModel>
}