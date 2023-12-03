package com.example.observationapp.repository.server

import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class ApiRepository @Inject constructor(private val apiServices: ApiServices) {

    suspend fun getLogin() = apiServices.getLoginApi(0)

}