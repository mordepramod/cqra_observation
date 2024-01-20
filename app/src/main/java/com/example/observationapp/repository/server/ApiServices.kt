package com.example.observationapp.repository.server

import com.example.observationapp.models.LoginResponseModel
import com.example.observationapp.models.ObservationHistoryModel
import com.example.observationapp.models.ProjectModel
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiServices {
    @GET("TEST/epr/index.php/api/getProjects/{userId}")
    suspend fun getProjectListAPI(@Path("userId") userId: String): Response<ProjectModel>

    @GET("TEST/epr/index.php/api/getObservations/{projectId}")
    suspend fun getObservationHistoryAPI(@Path("projectId") projectId: String): Response<ObservationHistoryModel>

    @POST("TEST/epr/index.php/api/login")
    suspend fun getLoginAPI(@Body json: JsonObject): Response<LoginResponseModel>
}