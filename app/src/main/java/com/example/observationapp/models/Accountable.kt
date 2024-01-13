package com.example.observationapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.observationapp.util.ApplicationDBTables

@Entity(tableName = ApplicationDBTables.TABLE_ACCOUNTABLE)
data class Accountable(
    val first_name: String,
    val last_name: String,
    @PrimaryKey
    val user_id: String
) {
    override fun toString(): String {
        return "$first_name $last_name"
    }
}