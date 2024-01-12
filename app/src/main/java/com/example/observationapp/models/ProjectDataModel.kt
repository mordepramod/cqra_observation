package com.example.observationapp.models

data class ProjectDataModel(
    val observation_data: ObservationData,
    val project_data: List<ProjectModelItem>
) {
    override fun toString(): String {
        return "ProjectDataModel(observation_data=$observation_data, project_data=$project_data)"
    }
}