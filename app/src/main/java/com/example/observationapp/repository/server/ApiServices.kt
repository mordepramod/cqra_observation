package com.example.observationapp.repository.server

import com.example.observationapp.models.ProjectModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiServices {
    @GET("TEST/epr/index.php/api/getProjects/{userId}")
    suspend fun getProjectListAPI(@Path("userId") page: Int): Response<ProjectModel>
}