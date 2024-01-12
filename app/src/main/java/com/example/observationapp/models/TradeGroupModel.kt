package com.example.observationapp.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.observationapp.util.ApplicationDBTables
import com.google.gson.annotations.SerializedName

@Entity(tableName = ApplicationDBTables.TABLE_TRADE_GROUP)
data class TradeGroupModel(
    @PrimaryKey
    val tradegroup_id: String,
    val tradegroup_name: String,
) {
    @Ignore
    @SerializedName("trades")
    val trades: List<TradeModel> = listOf()
    override fun toString(): String {
        return "TradeGroup(tradegroup_id='$tradegroup_id', tradegroup_name='$tradegroup_name',trades: $trades)"
    }
}