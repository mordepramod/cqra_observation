package com.example.observationapp.repository.database

import com.example.observationapp.models.Accountable
import com.example.observationapp.models.AllocatedToModel
import com.example.observationapp.models.ObservationCategory
import com.example.observationapp.models.ObservationSeverity
import com.example.observationapp.models.ObservationType
import com.example.observationapp.models.StatusModel
import com.example.observationapp.models.TradeGroupModel
import com.example.observationapp.models.TradeModel
import javax.inject.Inject

class ObservationListDBRepository @Inject constructor(
    private val observationDao: ObservationDao
) {
    suspend fun saveTradeGroupList(list: List<TradeGroupModel>): List<Long> =
        observationDao.insertTradeGroupModelList(list)

    suspend fun saveTradeModelList(list: List<TradeModel>): List<Long> =
        observationDao.insertTradeModelList(list)

    suspend fun saveObservationTypeList(list: List<ObservationType>): List<Long> =
        observationDao.insertObservationTypeList(list)

    suspend fun saveObservationSeverityList(list: List<ObservationSeverity>): List<Long> =
        observationDao.insertObservationSeverityList(list)

    suspend fun saveObservationCategoryList(list: List<ObservationCategory>): List<Long> =
        observationDao.insertObservationCategoryList(list)

    suspend fun saveAccountableList(list: List<Accountable>): List<Long> =
        observationDao.insertAccountableList(list)

    suspend fun saveAllocatedToList(list: List<AllocatedToModel>): List<Long> =
        observationDao.insertAllocatedToModelList(list)

    suspend fun saveAllStatusList(list: List<StatusModel>): List<Long> =
        observationDao.insertStatusModelList(list)

    fun getTradeGroupList(): List<TradeGroupModel> = observationDao.getTradeGroupList()

    fun getObservationTypeList(): List<ObservationType> =
        observationDao.getObservationTypeList()

    fun getObservationCategoryList(): List<ObservationCategory> =
        observationDao.getObservationCategoryList()

    fun getObservationSeverityList(): List<ObservationSeverity> =
        observationDao.getObservationSeverityList()

    fun getAccountableList(): List<Accountable> = observationDao.getAccountableList()

    suspend fun getTradeModelList(tradeGroupId: String): List<TradeModel> =
        observationDao.getTradeModelList(tradeGroupId)

    fun getAllocatedToList(): List<AllocatedToModel> =
        observationDao.getAllocatedToList()

    fun getAllStatusList(): List<StatusModel> =
        observationDao.getAllStatusToList()

    suspend fun deleteAllAccountable(): Int = observationDao.deleteAllAccountable()
    suspend fun deleteAllocatedTo(): Int = observationDao.deleteAllocatedTo()
    suspend fun deleteAllStatus(): Int = observationDao.deleteAllStatus()

    suspend fun deleteAllObservationCat(): Int = observationDao.deleteAllObservationCat()

    suspend fun deleteAllObservationSeverity(): Int = observationDao.deleteAllObservationSeverity()

    suspend fun deleteAllObservationType(): Int = observationDao.deleteAllObservationType()

    suspend fun deleteAllTradeGroup(): Int = observationDao.deleteAllTradeGroup()

}