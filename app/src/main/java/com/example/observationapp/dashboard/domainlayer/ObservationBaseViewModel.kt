package com.example.observationapp.dashboard.domainlayer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.observationapp.models.ObservationHistory

open class ObservationBaseViewModel : ViewModel() {
    val obsHistoryModel = MutableLiveData<ObservationHistory>()

    fun setObsHistoryModel(observationHistory: ObservationHistory) {
        obsHistoryModel.value = observationHistory
    }
}