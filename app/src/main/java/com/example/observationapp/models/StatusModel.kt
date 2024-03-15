package com.example.observationapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.observationapp.util.ApplicationDBTables

@Entity(tableName = ApplicationDBTables.TABLE_STATUS)
data class StatusModel(
    @PrimaryKey
    val status_id: String,
    val status_name: String
) {
    override fun toString(): String {
        return status_name
    }
}