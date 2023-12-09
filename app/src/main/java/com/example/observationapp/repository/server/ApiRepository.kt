package com.example.observationapp.repository.server

import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiServices: ApiServices) : ApiHelper {
    override suspend fun getProjectListAPI() = apiServices.getProjectListAPI(2)

}