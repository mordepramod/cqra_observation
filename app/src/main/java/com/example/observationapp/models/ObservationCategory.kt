package com.example.observationapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.observationapp.util.ApplicationDBTables

@Entity(tableName = ApplicationDBTables.TABLE_OBSERVATION_CATEGORY)
data class ObservationCategory(
    @PrimaryKey
    val category_id: String,
    val category_name: String
) {
    override fun toString(): String {
        return "ObservationCategory(category_id='$category_id', category_name='$category_name')"
    }
}