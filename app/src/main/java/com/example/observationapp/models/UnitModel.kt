package com.example.observationapp.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.observationapp.util.ApplicationDBTables
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = ApplicationDBTables.TABLE_UNIT,
    foreignKeys = [ForeignKey(
        entity = StageModel::class,
        parentColumns = arrayOf("stage_id"),
        childColumns = arrayOf("stage_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class UnitModel(
    @PrimaryKey()
    val unit_id: String,
    val stage_id: String,
    val unit_area: String,
    val unit_code: String,
    val unit_type: String
) : Parcelable {
    @Ignore
    var subunit: List<SubUnitModel> = listOf()
    override fun toString(): String {
        return unit_area
    }


}