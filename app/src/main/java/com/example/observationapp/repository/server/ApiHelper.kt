package com.example.observationapp.repository.server

import com.example.observationapp.models.ProjectModelList
import retrofit2.Response

interface ApiHelper {
    suspend fun getProjectListAPI(): Response<ProjectModelList>
}