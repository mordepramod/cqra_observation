package com.example.observationapp.login.presentation.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.observationapp.LoginViewModel
import com.example.observationapp.R
import com.example.observationapp.dashboard.presentationlayer.ui.activity.DashboardActivity
import com.example.observationapp.databinding.ActivityLoginBinding
import com.example.observationapp.util.Utility.launchActivity
import com.example.observationapp.util.showShortToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        clickListeners()
        setContentView(binding.root)
        liveDataObservers()
    }

    private fun liveDataObservers() {

        viewModel.loginUserInfo.observe(this) {
            it?.let {
                if (it > 0) {
                    viewModel.saveUserLoggedIn(true)
                    launchActivity<DashboardActivity> {
                        finish()
                    }
                } else {
                    showShortToast(getString(R.string.something_is_wrong))
                }
            }
        }
    }


    private fun clickListeners() {

        binding.btnLogin.setOnClickListener {
            login()
            //launchActivity<DashboardActivity>()
        }

    }

    private fun login() {
        val username = binding.tiedtUsername?.text.toString()
        val password = binding.tiedPassword?.text.toString()
        if (viewModel.checkUserName(username) && viewModel.checkPassword(password)) {
            viewModel.callLoginAPI(username, password)
        } else {
            showShortToast("Please enter login credentials.")
        }

    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}