package com.example.observationapp.dashboard.domainlayer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.observationapp.models.ProjectModelItem
import com.example.observationapp.util.APIResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor() : ViewModel() {

    companion object {
        private const val TAG = "DashboardViewModel"
    }

    var apiSuccess: Boolean = false
    private var _projectList = MutableLiveData<Boolean>()
    val projectList: LiveData<Boolean> = _projectList

    @Inject
    lateinit var projectListRepo: ProjectListUseCase
    fun getProjectsList() {
        val startTime = System.currentTimeMillis()
        viewModelScope.launch {
            val res = projectListRepo.getProjectListFlow()
            res.collect {

                when (it.status) {
                    APIResult.Status.SUCCESS -> {
                        apiSuccess = true
                        saveDb(it.data!!.result, startTime)
                    }

                    APIResult.Status.ERROR -> {
                        _projectList.value = false
                        Log.e(TAG, "getProjectsList:error ${it.message} ")
                    }
                }

            }
        }
    }

    private fun saveDb(list: List<ProjectModelItem>, startTime: Long) {
        viewModelScope.launch {
            projectListRepo.deleteAll()
            val projectValue = projectListRepo.saveProjectList(list)
            list.forEach { projectModelItem ->
                Log.e(TAG, "saveDb: projectModelItem ${projectModelItem.structureModel}")
                if (projectModelItem.structureModel.isNotEmpty()) {
                    val structureValue =
                        projectListRepo.saveStructureList(projectModelItem.structureModel)
                    Log.e(TAG, "saveDb structureModel: $structureValue")
                    projectModelItem.structureModel.forEach { structuralModelValue ->
                        if (structuralModelValue.stageModels.isNotEmpty()) {
                            val stageValue =
                                projectListRepo.saveStageModelsList(structuralModelValue.stageModels)
                            Log.e(TAG, "saveDb stageModels: $stageValue")
                            structuralModelValue.stageModels.forEach { stageModel ->
                                if (stageModel.unitModels.isNotEmpty()) {
                                    val unitValue =
                                        projectListRepo.saveUnitModelsList(stageModel.unitModels)
                                    Log.e(TAG, "saveDb unitModels: $unitValue")
                                    stageModel.unitModels.forEach {
                                        if (it.subunit.isNotEmpty()) {
                                            val subUnitValue =
                                                projectListRepo.saveSubUnitModelsList(it.subunit)
                                            Log.e(TAG, "saveDb subunit: $subUnitValue")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Log.e(TAG, "saveDb project: $projectValue")
            Log.d(
                TAG,
                "liveDataObservers: showProgress :${System.currentTimeMillis() - startTime}"
            )
            _projectList.value = true
        }
    }
}