package com.example.observationapp.login.domainlayer

import com.example.observationapp.models.LoginResponseModel
import com.example.observationapp.repository.database.ProjectDBRepository
import com.example.observationapp.util.APIResult
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LoginUseCase @Inject constructor() {
    @Inject
    lateinit var loginRepo: LoginRepo

    @Inject
    lateinit var projectDBRepository: ProjectDBRepository
    fun getLoginAPIFlow(jsonObject: JsonObject): Flow<APIResult<LoginResponseModel>> {
        return flow {
            val gdprResponse = loginRepo.getLoginAPI(jsonObject)
            emit(gdprResponse)
        }.flowOn(Dispatchers.IO)
    }

    companion object {
        const val TAG = "LoginUseCase"
    }
}