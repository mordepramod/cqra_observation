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

interface ApiHelper {
    suspend fun saveImagesAPI(
        surveyImage: List<MultipartBody.Part>,
        tempObsId: RequestBody,
        userId: String
    ): Response<ImageSaveResponseModel>

    suspend fun saveObservationFormAPI(
        userId: String,
        json: JsonObject
    ): Response<SingleObservationHistoryModel>

    suspend fun getObservationHistoryAPI(projectId: String): Response<ObservationHistoryModel>
    suspend fun getProjectListAPI(userId: String): Response<ProjectModel>
    suspend fun getLoginAPI(jsonObject: JsonObject): Response<LoginResponseModel>
}