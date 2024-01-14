package com.example.observationapp.login.domainlayer

import com.example.observationapp.models.LoginResponseModel
import com.example.observationapp.models.Module
import com.example.observationapp.models.Submodule
import com.example.observationapp.models.UserModel
import com.example.observationapp.repository.database.LoginDBRepository
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
    lateinit var loginDBRepository: LoginDBRepository
    fun getLoginAPIFlow(jsonObject: JsonObject): Flow<APIResult<LoginResponseModel>> {
        return flow {
            val gdprResponse = loginRepo.getLoginAPI(jsonObject)
            emit(gdprResponse)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun saveLoginUserInfo(user: UserModel): Long {
        return loginDBRepository.saveLogin(user)

    }

    suspend fun saveMenuModule(menu: Module): Long {
        return loginDBRepository.saveMenuModule(menu)
    }

    suspend fun saveMenuSubModule(submodules: List<Submodule>): List<Long> {
        return loginDBRepository.saveMenuSubModuleList(submodules)
    }

    companion object {
        const val TAG = "LoginUseCase"
    }
}