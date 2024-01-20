package com.example.observationapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.observationapp.util.ApplicationDBTables

@Entity(tableName = ApplicationDBTables.TABLE_OBSERVATION_TYPE)
data class ObservationType(
    @PrimaryKey
    val type_id: String,
    val type_name: String
) {
    override fun toString(): String {
        return type_name
    }
}