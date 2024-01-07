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
            val value = projectListRepo.saveProjectList(list)
            list.forEach {
                Log.e(TAG, "saveDb: ${it.structureModel}")
                val value = projectListRepo.saveStructureList(it.structureModel)
                Log.e(TAG, "saveDb structureModel: $value")
                it.structureModel.forEach {
                    val value = projectListRepo.saveStageModelsList(it.stageModels)
                    Log.e(TAG, "saveDb stageModels: $value")
                    it.stageModels.forEach {
                        val value = projectListRepo.saveUnitModelsList(it.unitModels)
                        Log.e(TAG, "saveDb unitModels: $value")
                        it.unitModels.forEach {
                            val value = projectListRepo.saveSubUnitModelsList(it.subunit)
                            Log.e(TAG, "saveDb subunit: $value")
                        }
                    }
                }
            }
            Log.e(TAG, "saveDb project: $value")
            Log.d(
                TAG,
                "liveDataObservers: showProgress :${System.currentTimeMillis() - startTime}"
            )

            _projectList.value = true
        }
    }
}