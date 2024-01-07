package com.example.observationapp.models

data class ProjectModel(
    val message: String,
    val result: List<ProjectModelItem>,
    val success: Boolean
) {
    override fun toString(): String {
        return "Project(message='$message', result=$result, success=$success)"
    }
}