package com.example.observationapp

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.observationapp.login.domainlayer.LoginUseCase
import com.example.observationapp.models.LoginResponseModel
import com.example.observationapp.util.APIResult
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    companion object {
        private const val TAG = "LoginViewModel"
    }

    @Inject
    lateinit var loginUseCase: LoginUseCase
    fun checkUserName(userName: String): Boolean {
        return !TextUtils.isEmpty(userName)
    }

    fun checkPassword(password: String): Boolean {
        return !TextUtils.isEmpty(password)
    }

    private fun getLoginGsonObject(username: String, password: String): JsonObject {
        val gson = JsonObject()
        gson.addProperty("number", username)
        gson.addProperty("password", password)

        return gson
    }

    fun callLoginAPI(username: String, password: String) {

        val jsonObject = getLoginGsonObject(username, password)
        viewModelScope.launch {
            val res = loginUseCase.getLoginAPIFlow(jsonObject)
            res.collect {

                when (it.status) {
                    APIResult.Status.SUCCESS -> {
                        saveLoginDataToBD(it.data)
                        Log.d(TAG, "callAPI:SUCCESS ${it.data}")
                    }

                    APIResult.Status.ERROR -> {
                        Log.e(TAG, "callLoginAPI:error ${it.message} ")
                    }
                }

            }
        }

    }

    private fun saveLoginDataToBD(data: LoginResponseModel?) {


    }
}
