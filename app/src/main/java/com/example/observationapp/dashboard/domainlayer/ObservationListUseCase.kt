package com.example.observationapp.dashboard.domainlayer

import com.example.observationapp.models.Accountable
import com.example.observationapp.models.AllocatedToModel
import com.example.observationapp.models.ObservationCategory
import com.example.observationapp.models.ObservationSeverity
import com.example.observationapp.models.ObservationType
import com.example.observationapp.models.StatusModel
import com.example.observationapp.models.TradeGroupModel
import com.example.observationapp.models.TradeModel
import com.example.observationapp.repository.database.ObservationListDBRepository
import javax.inject.Inject

class ObservationListUseCase @Inject constructor() {

    @Inject
    lateinit var observationDBRepo: ObservationListDBRepository

    suspend fun deleteAllAccountable(): Int {
        return observationDBRepo.deleteAllAccountable()
    }

    suspend fun deleteAllocatedTo(): Int {
        return observationDBRepo.deleteAllocatedTo()
    }

    suspend fun deleteAllStatus(): Int {
        return observationDBRepo.deleteAllStatus()
    }

    suspend fun deleteAllObservationCat(): Int {
        return observationDBRepo.deleteAllObservationCat()
    }

    suspend fun deleteAllObservationSeverity(): Int {
        return observationDBRepo.deleteAllObservationSeverity()
    }

    suspend fun deleteAllObservationType(): Int {
        return observationDBRepo.deleteAllObservationType()
    }

    suspend fun deleteAllTradeGroup(): Int {
        return observationDBRepo.deleteAllTradeGroup()
    }

    suspend fun saveAccountableList(list: List<Accountable>): List<Long> {
        return observationDBRepo.saveAccountableList(list)
    }

    suspend fun saveAllocatedToList(list: List<AllocatedToModel>): List<Long> {
        return observationDBRepo.saveAllocatedToList(list)
    }

    suspend fun saveAllStatusList(list: List<StatusModel>): List<Long> {
        return observationDBRepo.saveAllStatusList(list)
    }

    suspend fun saveObservationSeverityList(list: List<ObservationSeverity>): List<Long> {
        return observationDBRepo.saveObservationSeverityList(list)
    }

    suspend fun saveObservationCategoryList(list: List<ObservationCategory>): List<Long> {
        return observationDBRepo.saveObservationCategoryList(list)
    }

    suspend fun saveObservationTypeList(list: List<ObservationType>): List<Long> {
        return observationDBRepo.saveObservationTypeList(list)
    }

    suspend fun saveTradeGroupList(list: List<TradeGroupModel>): List<Long> {
        return observationDBRepo.saveTradeGroupList(list)
    }

    suspend fun saveTradeModelList(list: List<TradeModel>): List<Long> {
        return observationDBRepo.saveTradeModelList(list)
    }

    companion object {
        const val TAG = "ProjectListUseCase"
    }
}