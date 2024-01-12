package com.example.observationapp.models

data class ObservationData(
    val observation_category: List<ObservationCategory>,
    val observation_severity: List<ObservationSeverity>,
    val observation_type: List<ObservationType>,
    val trade_group: List<TradeGroup>
)