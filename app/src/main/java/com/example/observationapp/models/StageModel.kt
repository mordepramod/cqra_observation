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
    tableName = ApplicationDBTables.TABLE_STAGE,
    foreignKeys = [ForeignKey(
        entity = StructureModel::class,
        parentColumns = arrayOf("structure_id"),
        childColumns = arrayOf("structure_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class StageModel(
    @PrimaryKey
    val stage_id: String,
    @ColumnInfo(name = "structure_id", index = true)
    val structure_id: String,
    /*val _id :Int = 0,*/
    val stage_name: String
) : Parcelable {
    @Ignore
    @SerializedName("units")
    var unitModels: List<UnitModel> = listOf()
    override fun toString(): String {
        return stage_name
    }


}