package com.example.observationapp.dashboard.domainlayer

import com.example.observationapp.models.ProjectModelList
import com.example.observationapp.util.APIResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProjectListUseCase @Inject constructor() {
    @Inject
    lateinit var gdprRepo: ProjectListRepo
    fun getProjectListFlow(): Flow<APIResult<ProjectModelList>> {
        return flow {
            val gdprResponse = gdprRepo.getProjectListAPI()
            emit(gdprResponse)
        }.flowOn(Dispatchers.IO)

    }

    companion object {
        const val TAG = "GdprUseCase"
    }
}