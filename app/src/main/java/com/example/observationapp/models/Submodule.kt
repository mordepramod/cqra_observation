package com.example.observationapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.observationapp.util.ApplicationDBTables

@Entity(tableName = ApplicationDBTables.TABLE_MENU_SUBMODULE)
data class Submodule(
    @PrimaryKey
    val submodule_id: String,
    val submodule_name: String
) {
    override fun toString(): String {
        return "Submodule(submodule_id='$submodule_id', submodule_name='$submodule_name')"
    }
}