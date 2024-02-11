package com.example.observationapp.observation.observation_history.datalayer

import com.example.observationapp.models.ImageSaveResponseModel
import com.example.observationapp.models.ObservationHistory
import com.example.observationapp.models.ObservationHistoryModel
import com.example.observationapp.models.SingleObservationHistoryModel
import com.example.observationapp.observation.observation_history.domainlayer.ObservationHistoryRepo
import com.example.observationapp.repository.database.ObservationHistoryDBRepository
import com.example.observationapp.util.APIResult
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ObservationHistoryUseCase @Inject constructor() {

    @Inject
    lateinit var observationHistoryRepo: ObservationHistoryRepo

    @Inject
    lateinit var observationDBRepo: ObservationHistoryDBRepository

    suspend fun deleteAllObservationHistory(): Int {
        return observationDBRepo.deleteAllObservationHistory()
    }

    suspend fun saveObservationHistoryList(list: List<ObservationHistory>): List<Long> {
        return observationDBRepo.saveObservationHistoryList(list)
    }

    suspend fun getObservationHistoryList(): List<ObservationHistory> =
        observationDBRepo.getObservationHistoryList()

    fun saveObservationFormFlow(
        userId: String,
        json: JsonObject
    ): Flow<APIResult<SingleObservationHistoryModel>> {
        return flow {
            val gdprResponse = observationHistoryRepo.saveObservationFormAPI(userId, json)
            emit(gdprResponse)
        }.flowOn(Dispatchers.IO)
    }

    fun saveObservationImagesAPIFlow(
        surveyImage: List<MultipartBody.Part>,
        tempObsId: RequestBody,
        userId: String
    ): Flow<APIResult<ImageSaveResponseModel>> {
        return flow {
            val gdprResponse =
                observationHistoryRepo.saveObservationImagesAPI(surveyImage, tempObsId, userId)
            emit(gdprResponse)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun saveObservationImagesAPI(
        surveyImage: List<MultipartBody.Part>,
        tempObsId: RequestBody,
        userId: String
    ): APIResult<ImageSaveResponseModel> {
        return observationHistoryRepo.saveObservationImagesAPI(surveyImage, tempObsId, userId)
    }

    fun getObservationHistoryListFlow(userId: String): Flow<APIResult<ObservationHistoryModel>> {
        return flow {
            val gdprResponse = observationHistoryRepo.getObservationHistoryAPI(userId)
            emit(gdprResponse)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun insertObservationHistory(model: ObservationHistory): Long {
        return observationDBRepo.insertObservationHistory(model)
    }

    companion object {
        const val TAG = "ProjectListUseCase"
    }
}