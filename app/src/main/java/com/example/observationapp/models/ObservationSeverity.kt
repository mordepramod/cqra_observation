package com.example.observationapp.models

data class ObservationSeverity(
    val severity_id: String,
    val severity_name: String
) {
    override fun toString(): String {
        return "ObservationSeverity(severity_id='$severity_id', severity_name='$severity_name')"
    }
}