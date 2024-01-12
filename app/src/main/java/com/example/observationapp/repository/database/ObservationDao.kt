package com.example.observationapp.repository.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.observationapp.models.Accountable
import com.example.observationapp.models.ObservationCategory
import com.example.observationapp.models.ObservationSeverity
import com.example.observationapp.models.ObservationType
import com.example.observationapp.models.TradeGroupModel
import com.example.observationapp.models.TradeModel
import com.example.observationapp.util.ApplicationDBTables

@Dao
interface ObservationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccountableList(list: List<Accountable>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObservationCategoryList(list: List<ObservationCategory>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObservationSeverityList(list: List<ObservationSeverity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObservationTypeList(list: List<ObservationType>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTradeGroupModelList(list: List<TradeGroupModel>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTradeModelList(list: List<TradeModel>): List<Long>

    @Query("Delete from ${ApplicationDBTables.TABLE_ACCOUNTABLE}")
    suspend fun deleteAllAccountable(): Int

    @Query("Delete from ${ApplicationDBTables.TABLE_OBSERVATION_CATEGORY}")
    suspend fun deleteAllObservationCat(): Int

    @Query("Delete from ${ApplicationDBTables.TABLE_OBSERVATION_SEVERITY}")
    suspend fun deleteAllObservationSeverity(): Int

    @Query("Delete from ${ApplicationDBTables.TABLE_OBSERVATION_TYPE}")
    suspend fun deleteAllObservationType(): Int

    @Query("Delete from ${ApplicationDBTables.TABLE_TRADE_GROUP}")
    suspend fun deleteAllTradeGroup(): Int

}