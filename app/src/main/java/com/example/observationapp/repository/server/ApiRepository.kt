package com.example.observationapp.repository.server

import com.google.gson.JsonObject
import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiServices: ApiServices) : ApiHelper {
    override suspend fun getProjectListAPI() = apiServices.getProjectListAPI(6)
    override suspend fun getLoginAPI(jsonObject: JsonObject) = apiServices.getLoginAPI(jsonObject)

}