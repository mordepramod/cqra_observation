package com.example.observationapp.dashboard.domainlayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.observationapp.di.DataStoreRepoInterface
import com.example.observationapp.util.CommonConstant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {
    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    @Inject
    lateinit var dataStoreRepoInterface: DataStoreRepoInterface

    init {
        viewModelScope.launch {
            delay(3000L)
            _isReady.value = true
        }
    }

    fun isUserLoggedIn(): Boolean {
        return runBlocking {
            dataStoreRepoInterface.getBoolean(CommonConstant.USER_LOGGED_IN)
        }
    }
}