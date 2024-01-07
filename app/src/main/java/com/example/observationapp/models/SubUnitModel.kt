package com.example.observationapp.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.observationapp.util.ApplicationDBTables
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = ApplicationDBTables.TABLE_SUB_UNIT,
    foreignKeys = [ForeignKey(
        entity = UnitModel::class,
        parentColumns = arrayOf("unit_id"),
        childColumns = arrayOf("unit_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class SubUnitModel(
    @PrimaryKey
    val subunit_id: String,
    val subunit_name: String,
    val unit_id: String
) : Parcelable