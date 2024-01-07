package com.example.observationapp.models

data class ErrorModel(
    val message: String,
    val success: Boolean
) {
    override fun toString(): String {
        return "ErrorModel(message='$message', success=$success)"
    }
}