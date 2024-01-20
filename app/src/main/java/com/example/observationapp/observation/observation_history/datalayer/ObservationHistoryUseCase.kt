package com.example.observationapp.observation.observation_history.datalayer

import com.example.observationapp.models.ObservationHistory
import com.example.observationapp.models.ObservationHistoryModel
import com.example.observationapp.observation.observation_history.domainlayer.ObservationHistoryRepo
import com.example.observationapp.repository.database.ObservationHistoryDBRepository
import com.example.observationapp.util.APIResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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

    fun getObservationHistoryListFlow(userId: String): Flow<APIResult<ObservationHistoryModel>> {
        return flow {
            val gdprResponse = observationHistoryRepo.getObservationHistoryAPI(userId)
            emit(gdprResponse)
        }.flowOn(Dispatchers.IO)
    }


    companion object {
        const val TAG = "ProjectListUseCase"
    }
}