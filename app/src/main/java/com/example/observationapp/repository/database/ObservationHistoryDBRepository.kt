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

    suspend fun updateObservationHistory(
        isImagesUpload: Boolean,
        observationImage: List<String>,
        primaryId: Int
    ): Int =
        observationHistoryDao.updateObservationHistory(isImagesUpload, observationImage, primaryId)

    suspend fun updateFormObservationHistory(
        isFormUpload: Boolean,
        observationNumber: String,
        primaryId: Int
    ): Int =
        observationHistoryDao.updateFormObservationHistory(
            isFormUpload,
            observationNumber,
            primaryId
        )

    suspend fun getObservationHistoryList(): List<ObservationHistory> =
        observationHistoryDao.getObservationHistoryList()

    suspend fun getOfflineObservationHistoryList(): List<ObservationHistory> =
        observationHistoryDao.getOfflineObservationHistoryList()

    suspend fun getOfflineObservationFormHistoryList(): List<ObservationHistory> =
        observationHistoryDao.getOfflineObservationFormHistoryList()

    suspend fun deleteAllObservationHistory(): Int =
        observationHistoryDao.deleteAllObservationHistory()


}