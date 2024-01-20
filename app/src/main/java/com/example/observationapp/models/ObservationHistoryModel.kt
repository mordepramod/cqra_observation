package com.example.observationapp.models

data class ObservationHistoryModel(
    val message: String,
    val result: List<ObservationHistory>,
    val success: Boolean
)