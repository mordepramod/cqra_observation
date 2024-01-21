package com.example.observationapp.repository.database

import com.example.observationapp.models.ObservationHistory
import javax.inject.Inject

class ObservationHistoryDBRepository @Inject constructor(
    private val observationHistoryDao: ObservationHistoryDao
) {
    suspend fun saveObservationHistoryList(list: List<ObservationHistory>): List<Long> =
        observationHistoryDao.insertObservationHistoryList(list)

    suspend fun insertObservationHistory(model: ObservationHistory): Long =
        observationHistoryDao.insertObservationHistory(model)

    suspend fun getObservationHistoryList(): List<ObservationHistory> =
        observationHistoryDao.getObservationHistoryList()

    suspend fun deleteAllObservationHistory(): Int =
        observationHistoryDao.deleteAllObservationHistory()


}