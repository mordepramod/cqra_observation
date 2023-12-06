package com.example.observationapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.observationapp.dashboard.presentationlayer.ui.activity.DashboardActivity
import com.example.observationapp.databinding.ActivityLoginBinding
import com.example.observationapp.util.Utility.launchActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        clickListeners()
        setContentView(binding.root)
    }


    private fun clickListeners() {
        binding.btnLogin.setOnClickListener {
            launchActivity<DashboardActivity>()
        }

    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}