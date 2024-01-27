package com.example.observationapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.observationapp.util.ApplicationDBTables

@Entity(tableName = ApplicationDBTables.TABLE_ALLOCATED_TO)
data class AllocatedToModel(
    val created_date: String,
    val role_access: String,
    @PrimaryKey
    val role_id: String,
    val role_title: String
) {
    override fun toString(): String {
        return role_title
    }
}
