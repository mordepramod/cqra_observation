package com.example.observationapp.repository.server

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET("sdfs")
    suspend fun getLoginApi(@Query("") page: Int): Response<String>
}