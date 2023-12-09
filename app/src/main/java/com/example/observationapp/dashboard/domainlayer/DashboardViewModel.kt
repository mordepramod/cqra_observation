package com.example.observationapp.dashboard.domainlayer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.observationapp.models.ProjectModelList
import com.example.observationapp.util.APIResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor() : ViewModel() {

    companion object {
        private const val TAG = "DashboardViewModel"
    }

    private var _projectList = MutableLiveData<ProjectModelList>()
    val projectList: LiveData<ProjectModelList> = _projectList

    @Inject
    lateinit var projectListRepo: ProjectListUseCase
    fun getProjectsList() {
        viewModelScope.launch {

            val res = projectListRepo.getProjectListFlow()
            res.collect {

                when (it.status) {
                    APIResult.Status.SUCCESS -> {
                        _projectList.value = it.data!!
                    }

                    APIResult.Status.ERROR -> {
                        _projectList.value = it.data!!
                        Log.e(TAG, "getProjectsList:error ${it.message} ")
                    }
                }

            }
        }
    }
}