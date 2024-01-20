package com.example.observationapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.observationapp.util.ApplicationDBTables

@Entity(tableName = ApplicationDBTables.TABLE_OBSERVATION_SEVERITY)
data class ObservationSeverity(
    @PrimaryKey
    val severity_id: String,
    val severity_name: String
) {
    override fun toString(): String {
        return severity_name
    }
}