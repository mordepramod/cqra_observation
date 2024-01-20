package com.example.observationapp.repository.database

import androidx.lifecycle.LiveData
import com.example.observationapp.models.Accountable
import com.example.observationapp.models.ObservationCategory
import com.example.observationapp.models.ObservationSeverity
import com.example.observationapp.models.ObservationType
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

    fun getTradeGroupList(): LiveData<List<TradeGroupModel>> = observationDao.getTradeGroupList()

    fun getObservationTypeList(): LiveData<List<ObservationType>> =
        observationDao.getObservationTypeList()

    fun getObservationSeverityList(): LiveData<List<ObservationSeverity>> =
        observationDao.getObservationSeverityList()

    fun getAccountableList(): LiveData<List<Accountable>> = observationDao.getAccountableList()

    suspend fun getTradeModelList(tradeGroupId: String): List<TradeModel> =
        observationDao.getTradeModelList(tradeGroupId)

    suspend fun deleteAllAccountable(): Int = observationDao.deleteAllAccountable()

    suspend fun deleteAllObservationCat(): Int = observationDao.deleteAllObservationCat()

    suspend fun deleteAllObservationSeverity(): Int = observationDao.deleteAllObservationSeverity()

    suspend fun deleteAllObservationType(): Int = observationDao.deleteAllObservationType()

    suspend fun deleteAllTradeGroup(): Int = observationDao.deleteAllTradeGroup()

}