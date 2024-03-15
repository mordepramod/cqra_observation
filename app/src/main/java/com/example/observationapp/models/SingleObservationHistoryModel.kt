package com.example.observationapp.models

data class SingleObservationHistoryModel(
    val message: String,
    val result: ObservationHistory,
    val success: Boolean
)