package com.example.observationapp.dashboard.domainlayer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.observationapp.di.DataStoreRepoInterface
import com.example.observationapp.models.ObservationHistory
import com.example.observationapp.observation.observation_history.datalayer.ObservationHistoryUseCase
import com.example.observationapp.util.APIResult
import com.example.observationapp.util.CommonConstant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor() : ObservationBaseViewModel() {
    companion object {
        private const val TAG = "HistoryViewModel"
    }

    @Inject
    lateinit var dataStoreRepoInterface: DataStoreRepoInterface
    var apiSuccess: Boolean = false
    private var _observationHistory = MutableLiveData<Boolean>()
    val observationHistory: LiveData<Boolean> = _observationHistory
    private var _observationHistoryList = MutableLiveData<List<ObservationHistory>>()
    val observationHistoryList: LiveData<List<ObservationHistory>> = _observationHistoryList

    @Inject
    lateinit var observationHistoryUseCase: ObservationHistoryUseCase

    fun observationHistoryList() {
        viewModelScope.launch {
            _observationHistoryList.value = observationHistoryUseCase.getObservationHistoryList()
        }
    }

    fun getObservationHistoryAPI() {
        viewModelScope.launch {
            val userId = dataStoreRepoInterface.getString(CommonConstant.USER_ID)
            if (!userId.isNullOrEmpty()) {
                observationHistoryUseCase.getObservationHistoryListFlow(userId).collect { api ->
                    when (api.status) {
                        APIResult.Status.SUCCESS -> {
                            api.data?.let {
                                //if (it.success){
                                apiSuccess = true
                                saveObservationHistoryData(it.result)
                                //}
                            }

                        }

                        APIResult.Status.ERROR -> {
                            _observationHistory.value = false
                        }
                    }
                }
            } else {
                _observationHistoryList.value = arrayListOf()
            }

        }
    }

    private fun saveObservationHistoryData(result: List<ObservationHistory>) {

        viewModelScope.launch {
            deleteAllObservationHistory()
            val saveHistory = observationHistoryUseCase.saveObservationHistoryList(result)
            _observationHistory.value = true
            _observationHistoryList.value = result
            Log.d(TAG, "saveObservationHistoryData: saveObHistory: $saveHistory")
        }
    }

    private suspend fun deleteAllObservationHistory() {
        viewModelScope.async {
            val value = observationHistoryUseCase.deleteAllObservationHistory()
            Log.d(TAG, "deleteAllObservationHistory: $value")
        }.await()

    }


    fun putObservationHistoryApiCalled(isSuccess: Boolean) {
        viewModelScope.launch {
            dataStoreRepoInterface.putBoolean(
                CommonConstant.GET_OBSERVATION_HISTORY_API_CALLED,
                isSuccess
            )
        }
    }

    fun getObservationHistoryApiCalled(): Boolean {
        return runBlocking {
            dataStoreRepoInterface.getBoolean(CommonConstant.GET_OBSERVATION_HISTORY_API_CALLED)
        }
    }

}