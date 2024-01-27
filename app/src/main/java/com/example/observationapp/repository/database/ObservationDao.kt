package com.example.observationapp.repository.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.observationapp.models.Accountable
import com.example.observationapp.models.AllocatedToModel
import com.example.observationapp.models.ObservationCategory
import com.example.observationapp.models.ObservationSeverity
import com.example.observationapp.models.ObservationType
import com.example.observationapp.models.StatusModel
import com.example.observationapp.models.TradeGroupModel
import com.example.observationapp.models.TradeModel
import com.example.observationapp.util.ApplicationDBTables

@Dao
interface ObservationDao {

    /*******************    Insert Data into DB Starts     ********************/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllocatedToModelList(list: List<AllocatedToModel>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStatusModelList(list: List<StatusModel>): List<Long>

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
    /*******************    Insert Data into DB Ends     ********************/


    /*******************    Get Data from DB Starts     ********************/
    @Query("SELECT * FROM ${ApplicationDBTables.TABLE_TRADE_GROUP}")
    fun getTradeGroupList(): LiveData<List<TradeGroupModel>>

    @Query("SELECT * FROM ${ApplicationDBTables.TABLE_OBSERVATION_TYPE}")
    fun getObservationTypeList(): LiveData<List<ObservationType>>

    @Query("SELECT * FROM ${ApplicationDBTables.TABLE_OBSERVATION_CATEGORY}")
    fun getObservationCategoryList(): LiveData<List<ObservationCategory>>

    @Query("SELECT * FROM ${ApplicationDBTables.TABLE_OBSERVATION_SEVERITY}")
    fun getObservationSeverityList(): LiveData<List<ObservationSeverity>>

    @Query("SELECT * FROM ${ApplicationDBTables.TABLE_ACCOUNTABLE}")
    fun getAccountableList(): LiveData<List<Accountable>>

    @Query("SELECT * FROM ${ApplicationDBTables.TABLE_ALLOCATED_TO}")
    fun getAllocatedToList(): LiveData<List<AllocatedToModel>>

    @Query("SELECT * FROM ${ApplicationDBTables.TABLE_STATUS}")
    fun getAllStatusToList(): LiveData<List<StatusModel>>

    @Query("SELECT * FROM ${ApplicationDBTables.TABLE_TRADE_MODEL} WHERE tradegroup_id = :tradeGroupId")
    suspend fun getTradeModelList(tradeGroupId: String): List<TradeModel>


    /*******************    Get Data from DB Ends     ********************/

    /*******************    delete Data from DB Starts     ********************/

    @Query("Delete from ${ApplicationDBTables.TABLE_STATUS}")
    suspend fun deleteAllStatus(): Int

    @Query("Delete from ${ApplicationDBTables.TABLE_ALLOCATED_TO}")
    suspend fun deleteAllocatedTo(): Int

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

    /*******************    delete Data from DB ends     ********************/

}