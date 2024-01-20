package com.example.observationapp.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.observationapp.util.ApplicationDBTables
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(
    tableName = ApplicationDBTables.TABLE_STRUCTURE,
    foreignKeys = [ForeignKey(
        entity = ProjectModelItem::class,
        parentColumns = arrayOf("project_id"),
        childColumns = arrayOf("project_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class StructureModel(
    @PrimaryKey
    val structure_id: String = "",
    /*val _id :Int = 0,*/
    val created_date: String = "",
    @ColumnInfo(name = "project_id", index = true)
    val project_id: String = "",
    val structure_area: String = "",
    val structure_floors: String = "",

    val structure_name: String = ""
) : Parcelable {
    @Ignore
    @SerializedName("stages")
    var stageModels: List<StageModel> = listOf()
    override fun toString(): String {
        return structure_name
    }
}