package com.example.observationapp.dashboard.presentationlayer.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.example.observationapp.LoginActivity
import com.example.observationapp.dashboard.domainlayer.SplashViewModel
import com.example.observationapp.util.Utility.launchActivity

class SplashScrActivity : AppCompatActivity() {

    private lateinit var viewModel: SplashViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SplashViewModel::class.java]
        initSplashScreen()
    }

    private fun initSplashScreen() {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                if (viewModel.isReady.value) {
                    Log.e(TAG, "initSplashScreen: ${viewModel.isReady.value}")
                    launchActivity<LoginActivity>() {
                        finish()
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