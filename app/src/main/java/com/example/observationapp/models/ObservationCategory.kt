package com.example.observationapp.models

data class ObservationCategory(
    val category_id: String,
    val category_name: String
) {
    override fun toString(): String {
        return "ObservationCategory(category_id='$category_id', category_name='$category_name')"
    }
}