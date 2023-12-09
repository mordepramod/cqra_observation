package com.example.observationapp.models

data class StageModel(
    val stage_id: String,
    val stage_name: String,
    val structure_id: String,
    val unitModels: List<UnitModel>
)