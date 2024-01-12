package com.example.observationapp.repository.database

import androidx.lifecycle.LiveData
import com.example.observationapp.models.ProjectModelItem
import com.example.observationapp.models.StageModel
import com.example.observationapp.models.StructureModel
import com.example.observationapp.models.SubUnitModel
import com.example.observationapp.models.UnitModel
import javax.inject.Inject

class ProjectDBRepository @Inject constructor(
    private val projectDao: ProjectDao
) {
    suspend fun saveAllProjectList(list: List<ProjectModelItem>): List<Long> =
        projectDao.insertProjectList(list)

    suspend fun saveStructureList(list: List<StructureModel>): List<Long> =
        projectDao.insertStructureList(list)

    suspend fun saveStageModelsList(list: List<StageModel>): List<Long> =
        projectDao.saveStageModelsList(list)

    suspend fun saveUnitModelsList(list: List<UnitModel>): List<Long> =
        projectDao.saveUnitModelsList(list)

    suspend fun saveSubUnitModelsList(list: List<SubUnitModel>): List<Long> =
        projectDao.saveSubUnitModelsList(list)

    suspend fun deleteAll(): Int = projectDao.deleteAll()

    suspend fun getStructureList(projectId: String): List<StructureModel> {
        return projectDao.getStructureListOnProjectId(projectId)
    }

    suspend fun getStageOrFloorList(structureId: String): List<StageModel> {
        return projectDao.getStageOrFloorList(structureId)
    }

    suspend fun getUnitList(stageOrFloorId: String): List<UnitModel> {
        return projectDao.getUnitList(stageOrFloorId)
    }

    val projectList: LiveData<List<ProjectModelItem>> = projectDao.getAllProjectList()
}