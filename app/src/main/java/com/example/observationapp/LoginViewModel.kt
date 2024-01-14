package com.example.observationapp

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.observationapp.di.DataStoreRepoInterface
import com.example.observationapp.login.domainlayer.LoginUseCase
import com.example.observationapp.models.LoginResultModel
import com.example.observationapp.util.APIResult
import com.example.observationapp.util.CommonConstant
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    companion object {
        private const val TAG = "LoginViewModel"
    }

    private var _loginUserInfo = MutableLiveData<Long>()
    var loginUserInfo: LiveData<Long> = _loginUserInfo

    @Inject
    lateinit var loginUseCase: LoginUseCase

    @Inject
    lateinit var dataStoreRepoInterface: DataStoreRepoInterface

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
            res.collect { api ->

                when (api.status) {
                    APIResult.Status.SUCCESS -> {
                        api.data?.let {
                            if (it.success) {
                                saveLoginDataToBD(it.result)
                            }
                        }

                        Log.d(TAG, "callAPI:SUCCESS ${api.data}")
                    }

                    APIResult.Status.ERROR -> {
                        Log.e(TAG, "callLoginAPI:error ${api.message} ")
                    }
                }

            }
        }

    }

    private fun saveLoginDataToBD(data: LoginResultModel) {
        viewModelScope.launch {
            _loginUserInfo.value = loginUseCase.saveLoginUserInfo(data.user)
        }
        viewModelScope.launch {
            data.menus.forEach {
                val menu = loginUseCase.saveMenuModule(it.module)
                Log.d(TAG, "saveLoginDataToBD menu: $menu ")
                val submenu = loginUseCase.saveMenuSubModule(it.submodules)
                Log.d(TAG, "saveLoginDataToBD submenu: $submenu ")
            }

        }
    }

    fun saveUserLoggedIn(isSuccess: Boolean) {
        viewModelScope.launch {
            dataStoreRepoInterface.putBoolean(CommonConstant.USER_LOGGED_IN, isSuccess)
        }
    }

}
