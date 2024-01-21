package com.example.observationapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.observationapp.util.ApplicationDBTables

@Entity(tableName = ApplicationDBTables.TABLE_OBSERVATION_HISTORY)
data class ObservationHistory(
    var activity_id: String = "",
    var client_id: String = "",
    var closed_by: String = "",
    var created_by: String = "",
    var description: String = "",
    var floors: List<Int> = arrayListOf(),
    var location: String = "",
    var observation_category: String = "",
    var observation_date: String = "",
    var observation_id: String = "",
    var observation_number: String = "",
    var observation_severity: String = "",
    var observation_type: String = "",
    var project_id: String = "",
    var reference: String = "",
    var remark: String = "",
    var site_representative: String = "",
    var status: String = "",
    var structure_id: String = "",
    var target_date: String = "",
    var tradegroup_id: String = "",
    @PrimaryKey(autoGenerate = true)
    var tempObservationId: Int = 0
) {
    override fun toString(): String {
        return "ObservationHistory(activity_id='$activity_id', client_id='$client_id', closed_by='$closed_by', created_by='$created_by', description='$description', floors=$floors, location='$location', observation_category='$observation_category', observation_date='$observation_date', observation_id='$observation_id', observation_number='$observation_number', observation_severity='$observation_severity', observation_type='$observation_type', project_id='$project_id', reference='$reference', remark='$remark', site_representative='$site_representative', status='$status', structure_id='$structure_id', target_date='$target_date', tradegroup_id='$tradegroup_id')"
    }
}