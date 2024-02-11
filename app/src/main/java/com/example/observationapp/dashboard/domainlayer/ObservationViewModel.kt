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
import com.google.gson.JsonArray
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


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

    fun getObservationCategoryList(): LiveData<List<ObservationCategory>> =
        observationDBRepo.getObservationCategoryList()

    fun getObservationSeverityList(): LiveData<List<ObservationSeverity>> =
        observationDBRepo.getObservationSeverityList()

    fun getAccountableList(): LiveData<List<Accountable>> = observationDBRepo.getAccountableList()

    fun getAllocatedToList(): LiveData<List<AllocatedToModel>> =
        observationDBRepo.getAllocatedToList()

    fun getAllStatusList(): LiveData<List<StatusModel>> = observationDBRepo.getAllStatusList()

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
        location: String,
        description: String,
        remark: String,
        reference: String,
        targetDate: String,
        savedPathList: ArrayList<String>,
        savedFileNameList: ArrayList<String>,
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

        /*        val json = JsonObject()
                json.addProperty("project_id", projectId)
                json.addProperty("structure_id", structureId)
                json.addProperty("floors", stageOrFloorId)
                json.addProperty("tradegroup_id", tradeGroupId)
                json.addProperty("activity_id", tradeId)
                json.addProperty("temp_observation_number", model.temp_observation_number)
                json.addProperty("observation_category", observationCategoryId)
                json.addProperty("observation_type", observationTypeId)
                json.addProperty("location", location)
                json.addProperty("description", description)
                json.addProperty("remark", remark)
                json.addProperty("reference", reference)
                json.addProperty("observation_severity", observationSeverityId)
                json.addProperty("site_representative", accountableId)
                json.addProperty("status", statusId)
                json.addProperty("closed_by", closeById)
                json.addProperty("observation_date", getTodayDateAndTime())
                json.addProperty("target_date", targetDate)
                json.add("observation_image", customisedImageList(savedFileNameList))
                Log.d(TAG, "saveForm: $json")*/

        //saveObservationHistoryAPI(json, model)

        viewModelScope.launch {
            _observationFormModel.value = false
            val value = saveObservationHistory(model)
            Log.d(TAG, "saveFormID : $value")
            _observationHistoryModel.value = value
        }
    }

    /*private fun saveObservationHistoryAPI(json: JsonObject, model: ObservationHistory) {
        var isSuccess = false
        viewModelScope.launch(Dispatchers.IO) {
            val saveForm = viewModelScope.async {
                val userId = dataStoreRepoInterface.getString(CommonConstant.USER_ID)
                if (!userId.isNullOrEmpty()) {
                    observationHistoryUseCase.saveObservationFormFlow(userId, json).collect { api ->
                        when (api.status) {
                            APIResult.Status.SUCCESS -> {
                                api.data?.let {
                                    //if (it.success){
                                    Log.e(TAG, "saveObservationHistoryAPI: ${api}")
                                    it.result.observation_image = model.observation_image
                                    saveObservationHistory(it.result)
                                    isSuccess = true
                                    //}
                                }


                            }

                            APIResult.Status.ERROR -> {
                                _observationFormModel.value = false
                                Log.e(TAG, "saveObservationHistoryAPI: ${api.status}")
                            }
                        }
                    }
                    return@async isSuccess
                } else {
                    _observationFormModel.value = false
                    Log.e(TAG, "saveObservationHistoryAPI: userID is empty")
                    return@async false
                }
            }
            val formUploaded = saveForm.await()
            Log.d(TAG, "saveObservationHistoryAPI: formUploaded - $formUploaded")
            if (formUploaded) {
                val saveImages = viewModelScope.async {
                    val userId = dataStoreRepoInterface.getString(CommonConstant.USER_ID)
                    if (!userId.isNullOrEmpty()) {
                        val requestBody =
                            model.temp_observation_number.toRequestBody(CommonConstant.MULTIPART.toMediaTypeOrNull())
                        observationHistoryUseCase.saveObservationImagesAPIFlow(
                            prepareFilePart(model.observation_image),
                            requestBody,
                            userId
                        ).collect { api ->
                            when (api.status) {
                                APIResult.Status.SUCCESS -> {
                                    api.data?.let {
                                        Log.d(
                                            TAG,
                                            "value - ${it.message}, saveObservationHistoryAPI: ${api.data}"
                                        )
                                        _observationFormModel.value = false
                                        _observationHistoryModel.value = savedId
                                    }

                                }

                                APIResult.Status.ERROR -> {
                                    Log.e(TAG, "saveObservationHistoryAPI: ${api.status}")
                                    _observationFormModel.value = false
                                }
                            }
                        }
                    } else {
                        _observationFormModel.value = false
                        Log.e(TAG, "saveObservationHistoryAPI: userID is empty")
                    }
                }
                saveImages.await()
            } else {
                //model.isImagesUpload = false
                viewModelScope.launch {
                    val updateModel = viewModelScope.async {
                        val value = observationHistoryRepo.updateObservationHistory(
                            model.isImagesUpload,
                            model.temp_observation_number,
                            model.primaryObservationId
                        )
                        Log.d(TAG, "saveObservationHistoryAPI: value: $value")
                        return@async value
                    }
                    val updateModelValue = updateModel.await()
                    Log.d(TAG, "saveObservationHistoryAPI: updateModelValue: $updateModelValue")
                    if (updateModelValue > 0) {
                        Log.d(TAG, "saveObservationHistoryAPI: Updated - savedId $savedId")
                        _observationFormModel.value = false
                        _observationHistoryModel.value = savedId
                    } else {
                        _observationFormModel.value = false
                    }
                }
            }
        }


    }*/

    private fun customisedImageList(savedPathList: ArrayList<String>): JsonArray {
        val jsonArray = JsonArray()
        savedPathList.forEachIndexed { index, path ->
            val getFileName =
                "${projectId}_${structureId}_${tradeId}_${path}_${index + 1}"
            jsonArray.add(getFileName)
        }

        return jsonArray
    }

    private suspend fun saveObservationHistory(model: ObservationHistory): Long {
        return viewModelScope.async {
            savedId = observationHistoryRepo.insertObservationHistory(model)
            Log.d(TAG, "inserted saveObservationHistory: savedId: $savedId")
            return@async savedId
        }.await()
    }


    fun addDummyEntry() {
        viewModelScope.launch {
            val list = arrayListOf<ObservationHistory>()
            for (i in 1..15) {
                val model = ObservationHistory()
                val savedPathList = arrayListOf<String>()
                if (i % 2 == 0) {
                    savedPathList.add("/storage/emulated/0/Pictures/1706992666110.png")
                } else {
                    savedPathList.add("/storage/emulated/0/Pictures/1706992686807.png")
                }
                model.observation_image = savedPathList
                model.temp_observation_number = "7887686856"
                list.add(model)
            }
            val saveHistory = observationHistoryRepo.saveObservationHistoryList(list)
            Log.d(TAG, "addDummyEntry: saveHistory: $saveHistory")
        }

    }

    companion object {
        private const val TAG = "ObservationViewModel"
    }
}