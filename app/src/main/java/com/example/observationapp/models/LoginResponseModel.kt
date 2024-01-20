package com.example.observationapp.models

data class LoginResponseModel(
    val message: String,
    val result: LoginResultModel,
    val success: Boolean
) {
    override fun toString(): String {
        return "LoginResponseModel(message='$message', result=$result, success=$success)"
    }
}