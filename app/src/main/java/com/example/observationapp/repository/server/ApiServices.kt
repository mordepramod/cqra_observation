package com.example.observationapp.repository.server

import com.example.observationapp.models.ImageSaveResponseModel
import com.example.observationapp.models.LoginResponseModel
import com.example.observationapp.models.ObservationHistoryModel
import com.example.observationapp.models.ProjectModel
import com.example.observationapp.models.SingleObservationHistoryModel
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiServices {
    @GET("TEST/epr/index.php/api/getProjects/{userId}")
    suspend fun getProjectListAPI(@Path("userId") userId: String): Response<ProjectModel>

    @Multipart
    @POST("TEST/epr/index.php/api/uploadImage/{userId}")
    suspend fun saveObservationImages(
        @Part surveyImage: List<MultipartBody.Part>,
        @Part("temp_observation_number") tempObsId: RequestBody,
        @Path("userId") userId: String
    ): Response<ImageSaveResponseModel>

    @GET("TEST/epr/index.php/api/getObservations/{projectId}")
    suspend fun getObservationHistoryAPI(@Path("projectId") projectId: String): Response<ObservationHistoryModel>

    @POST("TEST/epr/index.php/api/addObservation/{userId}")
    suspend fun saveObservationFormAPI(
        @Path("userId") userId: String,
        @Body json: JsonObject
    ): Response<SingleObservationHistoryModel>

    @POST("TEST/epr/index.php/api/login")
    suspend fun getLoginAPI(@Body json: JsonObject): Response<LoginResponseModel>
}