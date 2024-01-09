package com.example.observationapp.dashboard.domainlayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.observationapp.models.ProjectModelItem
import com.example.observationapp.models.StageModel
import com.example.observationapp.models.StructureModel
import com.example.observationapp.models.UnitModel
import com.example.observationapp.repository.database.ProjectDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ObservationViewModel @Inject constructor() : ViewModel() {
    @Inject
    lateinit var projectDBRepository: ProjectDBRepository

    private var _structureList = MutableLiveData<List<StructureModel>>()
    var structureList: LiveData<List<StructureModel>> = _structureList

    private var _stageOrFloorList = MutableLiveData<List<StageModel>>()
    var stageOrFloorList: LiveData<List<StageModel>> = _stageOrFloorList

    private var _unitList = MutableLiveData<List<UnitModel>>()
    var unitList: LiveData<List<UnitModel>> = _unitList

    fun getProjectList(): LiveData<List<ProjectModelItem>> {
        return projectDBRepository.projectList
    }

    fun getStructureList(projectId: String) {
        if (projectId.isNotEmpty()) {
            viewModelScope.launch {
                _structureList.value = projectDBRepository.getStructureList(projectId)
            }
        }
    }

    fun getStageOrFloorList(structureId: String) {
        if (structureId.isNotEmpty()) {
            viewModelScope.launch {
                _stageOrFloorList.value = projectDBRepository.getStageOrFloorList(structureId)
            }
        }
    }

    fun getUnitList(stageOrFloorId: String) {
        if (stageOrFloorId.isNotEmpty()) {
            viewModelScope.launch {
                _unitList.value = projectDBRepository.getUnitList(stageOrFloorId)
            }
        }
    }
}