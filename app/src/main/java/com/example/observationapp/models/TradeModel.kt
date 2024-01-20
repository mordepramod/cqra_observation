package com.example.observationapp.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.observationapp.util.ApplicationDBTables
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = ApplicationDBTables.TABLE_TRADE_MODEL,
    foreignKeys = [ForeignKey(
        entity = TradeGroupModel::class,
        parentColumns = arrayOf("tradegroup_id"),
        childColumns = arrayOf("tradegroup_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class TradeModel(
    @PrimaryKey
    val trade_id: String,
    val trade_name: String,
    @ColumnInfo(name = "tradegroup_id", index = true)
    val tradegroup_id: String
) : Parcelable {
    override fun toString(): String {
        return trade_name
    }
}