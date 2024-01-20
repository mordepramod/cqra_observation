package com.example.observationapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.observationapp.util.ApplicationDBTables

@Entity(tableName = ApplicationDBTables.TABLE_OBSERVATION_HISTORY)
data class ObservationHistory(
    val activity_id: String,
    val client_id: String,
    val closed_by: String,
    val created_by: String,
    val description: String,
    val floors: List<Int>,
    val location: String,
    val observation_category: String,
    val observation_date: String,
    @PrimaryKey
    val observation_id: String,
    val observation_number: String,
    val observation_severity: String,
    val observation_type: String,
    val project_id: String,
    val reference: String,
    val remark: String,
    val site_representative: String,
    val status: String,
    val structure_id: String,
    val target_date: String,
    val tradegroup_id: String
) {
    override fun toString(): String {
        return "ObservationHistory(activity_id='$activity_id', client_id='$client_id', closed_by='$closed_by', created_by='$created_by', description='$description', floors=$floors, location='$location', observation_category='$observation_category', observation_date='$observation_date', observation_id='$observation_id', observation_number='$observation_number', observation_severity='$observation_severity', observation_type='$observation_type', project_id='$project_id', reference='$reference', remark='$remark', site_representative='$site_representative', status='$status', structure_id='$structure_id', target_date='$target_date', tradegroup_id='$tradegroup_id')"
    }
}