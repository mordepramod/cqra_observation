package com.example.observationapp.models

data class ProjectModel(
    val message: String,
    val result: ProjectDataModel,
    val success: Boolean
) {
    override fun toString(): String {
        return "ProjectData(message='$message', result=$result, success=$success)"
    }
}