package com.example.observationapp.dashboard.domainlayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.observationapp.models.ProjectModelItem
import com.example.observationapp.repository.database.ProjectDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ObservationViewModel @Inject constructor() : ViewModel() {
    @Inject
    lateinit var projectDBRepository: ProjectDBRepository

    fun getProjectList(): LiveData<List<ProjectModelItem>> {
        return projectDBRepository.projectList
    }
}