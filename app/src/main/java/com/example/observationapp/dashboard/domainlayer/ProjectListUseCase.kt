package com.example.observationapp.dashboard.domainlayer

import com.example.observationapp.models.ProjectModel
import com.example.observationapp.models.ProjectModelItem
import com.example.observationapp.models.StageModel
import com.example.observationapp.models.StructureModel
import com.example.observationapp.models.SubUnitModel
import com.example.observationapp.models.UnitModel
import com.example.observationapp.repository.database.ProjectDBRepository
import com.example.observationapp.util.APIResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProjectListUseCase @Inject constructor() {
    @Inject
    lateinit var projectListRepo: ProjectListRepo

    @Inject
    lateinit var projectDBRepository: ProjectDBRepository
    fun getProjectListFlow(): Flow<APIResult<ProjectModel>> {
        return flow {
            val gdprResponse = projectListRepo.getProjectListAPI()
            emit(gdprResponse)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun deleteAll() {
        return projectDBRepository.deleteAll()
    }

    suspend fun saveProjectList(list: List<ProjectModelItem>): List<Long> {
        return projectDBRepository.saveAllProjectList(list)
    }

    suspend fun saveStructureList(list: List<StructureModel>): List<Long> {
        return projectDBRepository.saveStructureList(list)
    }

    suspend fun saveStageModelsList(list: List<StageModel>): List<Long> {
        return projectDBRepository.saveStageModelsList(list)
    }

    suspend fun saveUnitModelsList(list: List<UnitModel>): List<Long> {
        return projectDBRepository.saveUnitModelsList(list)
    }

    suspend fun saveSubUnitModelsList(list: List<SubUnitModel>): List<Long> {
        return projectDBRepository.saveSubUnitModelsList(list)
    }

    companion object {
        const val TAG = "ProjectListUseCase"
    }
}