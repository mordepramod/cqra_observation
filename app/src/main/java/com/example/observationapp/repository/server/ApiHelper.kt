package com.example.observationapp.repository.server

import com.example.observationapp.models.LoginResponseModel
import com.example.observationapp.models.ProjectModel
import com.google.gson.JsonObject
import retrofit2.Response

interface ApiHelper {
    suspend fun getProjectListAPI(): Response<ProjectModel>
    suspend fun getLoginAPI(jsonObject: JsonObject): Response<LoginResponseModel>
}