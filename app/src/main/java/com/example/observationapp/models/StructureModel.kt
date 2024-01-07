package com.example.observationapp.models

data class StructureModel(
    val created_date: String,
    val project_id: String,
    val stageModels: List<StageModel>,
    val structure_area: String,
    val structure_floors: String,
    val structure_id: String,
    val structure_name: String
)