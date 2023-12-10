package com.example.observationapp.repository.server

import com.example.observationapp.models.ProjectModel
import retrofit2.Response

interface ApiHelper {
    suspend fun getProjectListAPI(): Response<ProjectModel>
}