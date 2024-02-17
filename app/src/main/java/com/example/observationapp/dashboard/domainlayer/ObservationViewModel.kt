package com.example.observationapp.dashboard.domainlayer

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.observationapp.di.DataStoreRepoInterface
import com.example.observationapp.models.Accountable
import com.example.observationapp.models.AllocatedToModel
import com.example.observationapp.models.ObservationCategory
import com.example.observationapp.models.ObservationHistory
import com.example.observationapp.models.ObservationSeverity
import com.example.observationapp.models.ObservationType
import com.example.observationapp.models.ProjectModelItem
import com.example.observationapp.models.StageModel
import com.example.observationapp.models.StatusModel
import com.example.observationapp.models.StructureModel
import com.example.observationapp.models.TradeGroupModel
import com.example.observationapp.models.TradeModel
import com.example.observationapp.observation.observation_history.datalayer.ObservationHistoryUseCase
import com.example.observationapp.repository.database.ObservationHistoryDBRepository
import com.example.observationapp.repository.database.ObservationListDBRepository
import com.example.observationapp.repository.database.ProjectDBRepository
import com.example.observationapp.util.CommonConstant
import com.example.observationapp.util.Utility.getTodayDateAndTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.ContinuationInterceptor


@HiltViewModel
class ObservationViewModel @Inject constructor() : ViewModel() {
    private var savedId: Long = -1L

    @Inject
    lateinit var projectDBRepository: ProjectDBRepository

    @Inject
    lateinit var observationDBRepo: ObservationListDBRepository

    @Inject
    lateinit var observationHistoryRepo: ObservationHistoryDBRepository

    @Inject
    lateinit var dataStoreRepoInterface: DataStoreRepoInterface

    @Inject
    lateinit var observationHistoryUseCase: ObservationHistoryUseCase

    private var userId = ""

    private var _projectList = MutableLiveData<List<ProjectModelItem>>()
    var projectList: LiveData<List<ProjectModelItem>> = _projectList

    private var _observationTypeList = MutableLiveData<List<ObservationType>>()
    var observationTypeModelList: LiveData<List<ObservationType>> = _observationTypeList

    private var _observationCategoryList = MutableLiveData<List<ObservationCategory>>()
    var observationCategoryModelList: LiveData<List<ObservationCategory>> = _observationCategoryList

    private var _observationSeverityList = MutableLiveData<List<ObservationSeverity>>()
    var observationSeverList: LiveData<List<ObservationSeverity>> = _observationSeverityList

    private var _accountableList = MutableLiveData<List<Accountable>>()
    var accountableModelList: LiveData<List<Accountable>> = _accountableList

    private var _allocatedToModelList = MutableLiveData<List<AllocatedToModel>>()
    var allocatedlList: LiveData<List<AllocatedToModel>> = _allocatedToModelList

    private var _statusModelList = MutableLiveData<List<StatusModel>>()
    var statusModelList: LiveData<List<StatusModel>> = _statusModelList

    private var _tradeGroupModelList = MutableLiveData<List<TradeGroupModel>>()
    var tradeGroupModelList: LiveData<List<TradeGroupModel>> = _tradeGroupModelList

    private var _structureList = MutableLiveData<List<StructureModel>>()
    var structureList: LiveData<List<StructureModel>> = _structureList

    private var _stageOrFloorList = MutableLiveData<List<StageModel>>()
    var stageOrFloorList: LiveData<List<StageModel>> = _stageOrFloorList

    private var _tradeModelList = MutableLiveData<List<TradeModel>>()
    var tradeModelList: LiveData<List<TradeModel>> = _tradeModelList

    private var _observationHistoryModel = MutableLiveData<Long>()
    val observationHistoryModel: LiveData<Long> = _observationHistoryModel

    private var _observationFormModel = MutableLiveData<Boolean>()
    val observationFormModel: LiveData<Boolean> = _observationFormModel

    var projectId = ""
    var structureId = ""
    var stageOrFloorId = ""
    var tradeGroupId = ""
    var tradeId = ""
    var observationTypeId = ""
    var observationCategoryId = ""
    var observationSeverityId = ""
    var accountableId = ""
    var closeById = ""
    var statusId = ""

    fun getProjectList() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(
                TAG,
                "getProjectList Current Dispatcher: ${coroutineContext[ContinuationInterceptor.Key]}"
            )
            val result = projectDBRepository.getProjectList()
            withContext(Dispatchers.Main) {
                Log.d(
                    TAG,
                    "getProjectList Current Dispatcher: ${coroutineContext[ContinuationInterceptor.Key]}"
                )
                _projectList.value = result
            }
        }

    }

    fun getStructureList(projectId: String) {
        if (projectId.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                Log.d(
                    TAG,
                    "getStructureList Current Dispatcher: ${coroutineContext[ContinuationInterceptor.Key]}"
                )
                val result = projectDBRepository.getStructureList(projectId)
                withContext(Dispatchers.Main) {
                    Log.d(
                        TAG,
                        "getStructureList Current Dispatcher: ${coroutineContext[ContinuationInterceptor.Key]}"
                    )
                    _structureList.value = result
                }
            }
        }
    }

    fun getStageOrFloorList(structureId: String) {
        if (structureId.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) {
                    Log.d(
                        TAG,
                        "getStageOrFloorList Current Dispatcher: ${coroutineContext[ContinuationInterceptor.Key]}"
                    )
                    _stageOrFloorList.value = projectDBRepository.getStageOrFloorList(structureId)
                }
            }
        }
    }

    fun getTradeGroupList() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = observationDBRepo.getTradeGroupList()
            withContext(Dispatchers.Main) {
                Log.d(
                    TAG,
                    "getStageOrFloorList Current Dispatcher: ${coroutineContext[ContinuationInterceptor.Key]}"
                )
                _tradeGroupModelList.value = result
            }
        }

    }

    fun getObservationTypeList() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = observationDBRepo.getObservationTypeList()
            withContext(Dispatchers.Main) {
                Log.d(
                    TAG,
                    "getStageOrFloorList Current Dispatcher: ${coroutineContext[ContinuationInterceptor.Key]}"
                )
                _observationTypeList.value = result
            }
        }
    }


    fun getObservationCategoryList() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = observationDBRepo.getObservationCategoryList()
            withContext(Dispatchers.Main) {
                Log.d(
                    TAG,
                    "getStageOrFloorList Current Dispatcher: ${coroutineContext[ContinuationInterceptor.Key]}"
                )
                _observationCategoryList.value = result
            }
        }
    }

    fun getObservationSeverityList() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = observationDBRepo.getObservationSeverityList()
            withContext(Dispatchers.Main) {
                Log.d(
                    TAG,
                    "getStageOrFloorList Current Dispatcher: ${coroutineContext[ContinuationInterceptor.Key]}"
                )
                _observationSeverityList.value = result
            }
        }

    }

    fun getAccountableList() {
        viewModelScope.launch(Dispatchers.IO) {
            val result =
                observationDBRepo.getAccountableList()
            withContext(Dispatchers.Main) {
                Log.d(
                    TAG,
                    "getStageOrFloorList Current Dispatcher: ${coroutineContext[ContinuationInterceptor.Key]}"
                )
                _accountableList.value = result
            }
        }
    }

    fun getAllocatedToList() {
        viewModelScope.launch(Dispatchers.IO) {
            val result =
                observationDBRepo.getAllocatedToList()
            withContext(Dispatchers.Main) {
                Log.d(
                    TAG,
                    "getStageOrFloorList Current Dispatcher: ${coroutineContext[ContinuationInterceptor.Key]}"
                )
                _allocatedToModelList.value = result
            }
        }
    }

    fun getAllStatusList() {
        viewModelScope.launch(Dispatchers.IO) {
            val result =
                observationDBRepo.getAllStatusList()
            withContext(Dispatchers.Main) {
                Log.d(
                    TAG,
                    "getStageOrFloorList Current Dispatcher: ${coroutineContext[ContinuationInterceptor.Key]}"
                )
                _statusModelList.value = result
            }
        }
    }

    fun getTradeModelList(tradeGroupId: String) {
        if (tradeGroupId.isNotEmpty()) {
            viewModelScope.launch {
                Log.d(
                    TAG,
                    "getTradeModelList Current Dispatcher: ${coroutineContext[ContinuationInterceptor.Key]}"
                )
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
        location: String,
        description: String,
        remark: String,
        reference: String,
        targetDate: String,
        savedPathList: ArrayList<String>
    ) {
        _observationFormModel.value = true
        val model = ObservationHistory()
        model.project_id = projectId
        model.structure_id = structureId
        model.floors = stageOrFloorId
        model.tradegroup_id = tradeGroupId
        model.activityOrTradeId = tradeId
        model.observation_type = observationTypeId
        model.description = description
        model.remark = remark
        model.reference = reference
        model.observation_severity = observationSeverityId
        model.site_representative = accountableId
        model.created_by = userId
        model.observation_date = getTodayDateAndTime()
        model.location = location
        model.target_date = "$targetDate 00:00:00"
        model.status = statusId
        model.closed_by = closeById
        model.observation_image = savedPathList
        model.observation_category = observationCategoryId
        model.temp_observation_number = System.currentTimeMillis().toString()
        model.isOffline = true
        model.isImagesUpload = false
        Log.d(TAG, "saveForm: model : $model")

        viewModelScope.launch {
            _observationFormModel.value = false
            val value = saveObservationHistory(model)
            Log.d(TAG, "saveFormID : $value")
            _observationHistoryModel.value = value
        }
    }


    private suspend fun saveObservationHistory(model: ObservationHistory): Long {
        return viewModelScope.async {
            savedId = observationHistoryRepo.insertObservationHistory(model)
            Log.d(TAG, "inserted saveObservationHistory: savedId: $savedId")
            return@async savedId
        }.await()
    }

    companion object {
        private const val TAG = "ObservationViewModel"
    }
}