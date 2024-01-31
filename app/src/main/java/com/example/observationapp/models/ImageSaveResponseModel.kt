package com.example.observationapp.models

data class ImageSaveResponseModel(
    val message: String,
    val result: List<String>,
    val success: Boolean
) {
    override fun toString(): String {
        return "ImageSaveResponseModel(message='$message', result=$result, success=$success)"
    }
}