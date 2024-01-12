package com.example.observationapp.login.domainlayer

import android.util.Log
import com.example.observationapp.models.LoginResponseModel
import com.example.observationapp.repository.server.ApiHelper
import com.example.observationapp.util.APIResult
import com.google.gson.JsonObject
import java.net.HttpURLConnection
import javax.inject.Inject

class LoginRepo @Inject constructor() {
    companion object {
        private const val TAG = "LoginRepo"
    }

    @Inject
    lateinit var apiService: ApiHelper
    suspend fun getLoginAPI(jsonObject: JsonObject): APIResult<LoginResponseModel> {
        return try {
            val loginResponse = apiService.getLoginAPI(jsonObject)
            if (loginResponse.isSuccessful && loginResponse.code() == HttpURLConnection.HTTP_OK) {
                APIResult.success(loginResponse.body()!!)
            } else {
                APIResult.error(loginResponse.message(), null, null)
            }
        } catch (exception: Exception) {
            Log.e(TAG, "LoginRepo: ", exception)
            val errorMsg = exception.message ?: "error in LoginRepo $TAG"
            APIResult.error(errorMsg, null, exception)
        }
    }
}