package com.example.observationapp.repository.server

import com.example.observationapp.models.ImageSaveResponseModel
import com.example.observationapp.models.SingleObservationHistoryModel
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiServices: ApiServices) : ApiHelper {
    override suspend fun saveImagesAPI(
        surveyImage: List<MultipartBody.Part>,
        tempObsId: RequestBody,
        userId: String
    ): Response<ImageSaveResponseModel> =
        apiServices.saveObservationImages(surveyImage, tempObsId, userId)

    override suspend fun saveObservationFormAPI(
        userId: String,
        json: JsonObject
    ): Response<SingleObservationHistoryModel> =
        apiServices.saveObservationFormAPI(userId, json)

    override suspend fun getObservationHistoryAPI(projectId: String) =
        apiServices.getObservationHistoryAPI(projectId)

    override suspend fun getProjectListAPI(userId: String) = apiServices.getProjectListAPI(userId)
    override suspend fun getLoginAPI(jsonObject: JsonObject) = apiServices.getLoginAPI(jsonObject)

}