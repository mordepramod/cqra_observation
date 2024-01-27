package com.example.observationapp.models

import com.google.gson.annotations.SerializedName

data class ObservationData(
    val accountables: List<Accountable>,
    @SerializedName("status")
    val statusList: List<StatusModel>,
    val allocatedTo: List<AllocatedToModel>,
    val observation_category: List<ObservationCategory>,
    val observation_severity: List<ObservationSeverity>,
    val observation_type: List<ObservationType>,
    val trade_group: List<TradeGroupModel>
) {
    override fun toString(): String {
        return "ObservationData(accountables=$accountables, observation_category=$observation_category, observation_severity=$observation_severity, observation_type=$observation_type, trade_group=$trade_group)"
    }
}