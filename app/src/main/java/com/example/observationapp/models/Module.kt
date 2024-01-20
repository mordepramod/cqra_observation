package com.example.observationapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.observationapp.util.ApplicationDBTables

@Entity(tableName = ApplicationDBTables.TABLE_MENU_MODULE)
data class Module(
    @PrimaryKey
    val module_id: String,
    val module_name: String
) {
    override fun toString(): String {
        return "Module(module_id='$module_id', module_name='$module_name')"
    }
}