package com.example.observationapp.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.observationapp.util.ApplicationDBTables
import com.google.gson.annotations.SerializedName

@Entity(tableName = ApplicationDBTables.TABLE_PROJECT)
data class ProjectModelItem(
    @PrimaryKey
    val project_id: String = "",
    /*val _id: Int = 0,*/
    val address: String = "",
    val contact_number: String = "",
    val created_date: String = "",
    val developer_id: String = "",
    val email_id: String = "",
    val end_date: String = "",
    val gst_number: String = "",
    val mr_name: String = "",
    val project_location: String = "",
    val project_name: String = "",
    val project_type: String = "",
    val start_date: String = "",
    val status: String = "",
) {

    @Ignore
    @SerializedName("structure")
    var structureModel: List<StructureModel> = listOf()
    override fun toString(): String {
        return project_name
    }
}