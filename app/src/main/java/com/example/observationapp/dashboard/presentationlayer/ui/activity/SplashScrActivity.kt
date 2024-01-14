package com.example.observationapp.dashboard.presentationlayer.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.observationapp.dashboard.domainlayer.SplashViewModel
import com.example.observationapp.login.presentation.ui.activity.LoginActivity
import com.example.observationapp.util.Utility.launchActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScrActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initSplashScreen()
    }

    private fun initSplashScreen() {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                if (viewModel.isReady.value) {
                    Log.e(TAG, "initSplashScreen: ${viewModel.isReady.value}")
                    if (viewModel.isUserLoggedIn()) {
                        launchActivity<DashboardActivity>() {
                            finish()
                        }
                    } else {
                        launchActivity<LoginActivity>() {
                            finish()
                        }
                    }

                }
                !viewModel.isReady.value


            }
        }
    }

    companion object {
        private const val TAG = "SplashScreenActivity"
    }
}