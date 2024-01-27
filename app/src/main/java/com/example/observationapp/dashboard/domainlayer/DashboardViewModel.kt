package com.example.observationapp.dashboard.domainlayer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.observationapp.di.DataStoreRepoInterface
import com.example.observationapp.models.Module
import com.example.observationapp.models.ObservationData
import com.example.observationapp.models.ProjectDataModel
import com.example.observationapp.models.UserModel
import com.example.observationapp.repository.database.DatastoreRepoImpl
import com.example.observationapp.repository.database.LoginDBRepository
import com.example.observationapp.repository.database.ObservationHistoryDBRepository
import com.example.observationapp.util.APIResult
import com.example.observationapp.util.CommonConstant.GET_OBSERVATION_HISTORY_API_CALLED
import com.example.observationapp.util.CommonConstant.GET_PROJECTS_API_CALLED
import com.example.observationapp.util.CommonConstant.USER_ID
import com.example.observationapp.util.CommonConstant.USER_LOGGED_IN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor() : ViewModel() {

    companion object {
        private const val TAG = "DashboardViewModel"
    }

    var apiSuccess: Boolean = false
    private var _projectList = MutableLiveData<Boolean>()
    val projectList: LiveData<Boolean> = _projectList

    private var _userInfo = MutableLiveData<UserModel?>()
    val userInfo: LiveData<UserModel?> = _userInfo

    private var _modelList = MutableLiveData<List<Module>?>()
    val modelList: LiveData<List<Module>?> = _modelList

    @Inject
    lateinit var projectListRepo: ProjectListUseCase

    @Inject
    lateinit var dataStoreRepoInterface: DataStoreRepoInterface

    @Inject
    lateinit var observationListUseCase: ObservationListUseCase

    @Inject
    lateinit var observationHistoryDBRepository: ObservationHistoryDBRepository

    @Inject
    lateinit var loginDBRepository: LoginDBRepository
    fun getProjectsList(userId: String) {
        val startTime = System.currentTimeMillis()
        viewModelScope.launch {

            val res = projectListRepo.getProjectListFlow(userId)
            res.collect { it ->

                when (it.status) {
                    APIResult.Status.SUCCESS -> {
                        apiSuccess = true
                        it.data?.let {
                            saveProjectDb(it.result, startTime)
                        }

                    }

                    APIResult.Status.ERROR -> {
                        _projectList.value = false
                        Log.e(TAG, "getProjectsList:error ${it.message} ")
                    }
                }

            }
        }
    }

    private fun saveProjectDb(model: ProjectDataModel, startTime: Long) {
        viewModelScope.launch {
            deleteAllTablesData()
            Log.d(TAG, "saveProjectDb: Deleted all data")
            val list = model.project_data
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
            saveObservationDataToDb(model.observation_data, startTime)

        }
    }

    suspend fun deleteAllTablesData() {
        viewModelScope.async {
            projectListRepo.deleteAll()
            deleteAccountable()
            deleteStatusList()
            deleteAllocatedTo()
            deleteAllObservationCat()
            deleteAllObservationSeverity()
            deleteAllObservationType()
            deleteAllTradeGroup()
            deleteAllObservationHistory()
        }.await()

    }

    private suspend fun deleteAllObservationHistory(): Int {
        return viewModelScope.async(Dispatchers.IO) {
            val value = observationHistoryDBRepository.deleteAllObservationHistory()
            Log.d(TAG, "deleteAllObservationHistory: value: $value")
        }.await()


    }

    suspend fun deleteLoginRelatedData(): Int {
        return viewModelScope.async {
            deleteMenuModule()
            deleteMenuSubModule()
            deleteUserInfo()
        }.await()

    }

    fun deleteDataStore() {
        viewModelScope.launch {
            dataStoreRepoInterface.clearSharedPref(
                DatastoreRepoImpl.PreferenceType.BOOLEAN_PREF,
                GET_PROJECTS_API_CALLED
            )
            dataStoreRepoInterface.clearSharedPref(
                DatastoreRepoImpl.PreferenceType.BOOLEAN_PREF,
                USER_LOGGED_IN
            )
            dataStoreRepoInterface.clearSharedPref(
                DatastoreRepoImpl.PreferenceType.STRING_PREF,
                USER_ID
            )
            dataStoreRepoInterface.clearSharedPref(
                DatastoreRepoImpl.PreferenceType.BOOLEAN_PREF,
                GET_OBSERVATION_HISTORY_API_CALLED
            )
        }
    }

    private fun saveObservationDataToDb(model: ObservationData, startTime: Long) {
        viewModelScope.launch {

            if (model.accountables.isNotEmpty()) {
                val value = observationListUseCase.saveAccountableList(model.accountables)
                Log.e(TAG, "saveAccountableList: $value")
            }

            if (model.allocatedTo.isNotEmpty()) {
                val value = observationListUseCase.saveAllocatedToList(model.allocatedTo)
                Log.e(TAG, "allocatedTo: $value")
            }

            if (model.statusList.isNotEmpty()) {
                val value = observationListUseCase.saveAllStatusList(model.statusList)
                Log.e(TAG, "statusList: $value")
            }
            if (model.observation_severity.isNotEmpty()) {
                val value =
                    observationListUseCase.saveObservationSeverityList(model.observation_severity)
                Log.e(TAG, "saveObservationSeverityList: $value")
            }
            if (model.observation_category.isNotEmpty()) {
                val value =
                    observationListUseCase.saveObservationCategoryList(model.observation_category)
                Log.e(TAG, "saveObservationCategoryList: $value")
            }
            if (model.observation_type.isNotEmpty()) {
                val value = observationListUseCase.saveObservationTypeList(model.observation_type)
                Log.e(TAG, "observation_type: $value")
            }
            if (model.trade_group.isNotEmpty()) {
                val value = observationListUseCase.saveTradeGroupList(model.trade_group)
                Log.e(TAG, "trade_group: $value")
            }
            val list = model.trade_group
            list.forEach { item ->
                if (item.trades.isNotEmpty()) {
                    val tradeModelValue =
                        observationListUseCase.saveTradeModelList(item.trades)
                    Log.e(TAG, "saveDb tradeModel: $tradeModelValue")
                }
            }
            Log.d(
                TAG,
                "liveDataObservers: showProgress :${System.currentTimeMillis() - startTime}"
            )
            _projectList.value = true
        }
    }

    private suspend fun deleteAccountable(): Int {
        return viewModelScope.async(Dispatchers.IO) {
            val value = observationListUseCase.deleteAllAccountable()
            Log.d(TAG, "deleteAccountable: value: $value")
        }.await()
    }

    private suspend fun deleteAllocatedTo(): Int {
        return viewModelScope.async(Dispatchers.IO) {
            val value = observationListUseCase.deleteAllocatedTo()
            Log.d(TAG, "deleteAllocatedTo: value: $value")
        }.await()
    }

    private suspend fun deleteStatusList(): Int {
        return viewModelScope.async(Dispatchers.IO) {
            val value = observationListUseCase.deleteAllStatus()
            Log.d(TAG, "deleteStatusList: value: $value")
        }.await()
    }

    private suspend fun deleteAllObservationCat(): Int {
        return viewModelScope.async(Dispatchers.IO) {
            val value = observationListUseCase.deleteAllObservationCat()
            Log.d(TAG, "deleteAllObservationCat: value: $value")
        }.await()
    }

    private suspend fun deleteAllObservationSeverity(): Int {
        return viewModelScope.async(Dispatchers.IO) {
            val value = observationListUseCase.deleteAllObservationSeverity()
            Log.d(TAG, "deleteAllObservationSeverity: value: $value")
        }.await()
    }

    private suspend fun deleteAllObservationType(): Int {
        return viewModelScope.async(Dispatchers.IO) {
            val value = observationListUseCase.deleteAllObservationType()
            Log.d(TAG, "deleteAllObservationType: value: $value")
        }.await()
    }

    private suspend fun deleteAllTradeGroup(): Int {
        return viewModelScope.async(Dispatchers.IO) {
            val value = observationListUseCase.deleteAllTradeGroup()
            Log.d(TAG, "deleteAllTradeGroup: value: $value")
        }.await()
    }

    private suspend fun deleteMenuModule(): Int {
        return viewModelScope.async(Dispatchers.IO) {
            val value = loginDBRepository.deleteMenuModule()
            Log.d(TAG, "deleteMenuModule: value: $value")
        }.await()
    }

    private suspend fun deleteMenuSubModule(): Int {
        return viewModelScope.async(Dispatchers.IO) {
            val value = loginDBRepository.deleteAllMenuSubModuleList()
            Log.d(TAG, "deleteMenuSubModule: value: $value")
        }.await()
    }

    private suspend fun deleteUserInfo(): Int {
        return viewModelScope.async(Dispatchers.IO) {
            val value = loginDBRepository.deleteLoggedInUser()
            Log.d(TAG, "deleteUserInfo: value: $value")
        }.await()
    }

    fun putProjectsApiCalled(isSuccess: Boolean) {
        viewModelScope.launch {
            dataStoreRepoInterface.putBoolean(GET_PROJECTS_API_CALLED, isSuccess)
        }
    }

    fun getProjectsApiCalled(): Boolean {
        return runBlocking {
            dataStoreRepoInterface.getBoolean(GET_PROJECTS_API_CALLED)
        }
    }

    suspend fun getLoggedInUser() {
        _userInfo.value = loginDBRepository.getLoggedInUser()
    }

    suspend fun moduleList() {
        _modelList.value = loginDBRepository.getModuleData()
    }

    fun resetLiveData() {
        _modelList.value = null
        _userInfo.value = null
    }
}