package com.example.observationapp.dashboard.domainlayer

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.observationapp.di.DataStoreRepoInterface
import com.example.observationapp.models.Accountable
import com.example.observationapp.models.ObservationHistory
import com.example.observationapp.models.ObservationSeverity
import com.example.observationapp.models.ObservationType
import com.example.observationapp.models.ProjectModelItem
import com.example.observationapp.models.StageModel
import com.example.observationapp.models.StructureModel
import com.example.observationapp.models.TradeGroupModel
import com.example.observationapp.models.TradeModel
import com.example.observationapp.repository.database.ObservationHistoryDBRepository
import com.example.observationapp.repository.database.ObservationListDBRepository
import com.example.observationapp.repository.database.ProjectDBRepository
import com.example.observationapp.util.CommonConstant
import com.example.observationapp.util.Utility.getTodayDateAndTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ObservationViewModel @Inject constructor() : ViewModel() {
    @Inject
    lateinit var projectDBRepository: ProjectDBRepository

    @Inject
    lateinit var observationDBRepo: ObservationListDBRepository

    @Inject
    lateinit var observationHistoryRepo: ObservationHistoryDBRepository

    @Inject
    lateinit var dataStoreRepoInterface: DataStoreRepoInterface

    private var userId = ""

    private var _structureList = MutableLiveData<List<StructureModel>>()
    var structureList: LiveData<List<StructureModel>> = _structureList

    private var _stageOrFloorList = MutableLiveData<List<StageModel>>()
    var stageOrFloorList: LiveData<List<StageModel>> = _stageOrFloorList

    private var _tradeModelList = MutableLiveData<List<TradeModel>>()
    var tradeModelList: LiveData<List<TradeModel>> = _tradeModelList

    private var _observationHistoryModel = MutableLiveData<Long>()
    val observationHistoryModel: LiveData<Long> = _observationHistoryModel

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

    fun getTradeGroupList(): LiveData<List<TradeGroupModel>> = observationDBRepo.getTradeGroupList()

    fun getObservationTypeList(): LiveData<List<ObservationType>> =
        observationDBRepo.getObservationTypeList()

    fun getObservationSeverityList(): LiveData<List<ObservationSeverity>> =
        observationDBRepo.getObservationSeverityList()

    fun getAccountableList(): LiveData<List<Accountable>> = observationDBRepo.getAccountableList()

    fun getTradeModelList(tradeGroupId: String) {
        if (tradeGroupId.isNotEmpty()) {
            viewModelScope.launch {
                _tradeModelList.value = observationDBRepo.getTradeModelList(tradeGroupId)
            }
        }
    }

    fun getUserId() {
        runBlocking {
            userId = dataStoreRepoInterface.getString(CommonConstant.USER_ID) ?: ""
        }

    }

    fun isValueEmpty(str: String): Boolean {
        return TextUtils.isEmpty(str)
    }

    fun saveForm(
        projectId: String,
        structureId: String,
        stageOrFloorId: String,
        tradeGroupId: String,
        tradeId: String,
        observationTypeId: String,
        description: String,
        remark: String,
        reference: String,
        observationSeverityId: String,
        accountableId: String,
        savedPathList: ArrayList<String>
    ) {
        val model = ObservationHistory()
        model.project_id = projectId
        model.structure_id = structureId
        model.floors = arrayListOf(stageOrFloorId.toInt())
        model.tradegroup_id = tradeGroupId
        model.observation_type = observationTypeId
        model.description = description
        model.remark = remark
        model.reference = reference
        model.observation_severity = observationSeverityId
        model.site_representative = accountableId
        model.created_by = userId
        model.observation_date = getTodayDateAndTime()
        model.location = savedPathList.toString()
        saveObservationHistory(model)
    }

    private fun saveObservationHistory(model: ObservationHistory) {
        viewModelScope.launch {
            _observationHistoryModel.value = observationHistoryRepo.insertObservationHistory(model)
        }
    }
}