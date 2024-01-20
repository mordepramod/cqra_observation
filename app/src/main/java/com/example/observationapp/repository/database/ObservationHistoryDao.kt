package com.example.observationapp.repository.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.observationapp.models.ObservationHistory
import com.example.observationapp.util.ApplicationDBTables

@Dao
interface ObservationHistoryDao {

    /*******************    Insert Data into DB Starts     ********************/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObservationHistoryList(list: List<ObservationHistory>): List<Long>


    /*******************    Insert Data into DB Ends     ********************/


    /*******************    Get Data from DB Starts     ********************/
    @Query("SELECT * FROM ${ApplicationDBTables.TABLE_OBSERVATION_HISTORY}")
    suspend fun getObservationHistoryList(): List<ObservationHistory>

    /*******************    Get Data from DB Ends     ********************/

    /*******************    delete Data from DB Starts     ********************/

    @Query("Delete from ${ApplicationDBTables.TABLE_OBSERVATION_HISTORY}")
    suspend fun deleteAllObservationHistory(): Int

    /*******************    delete Data from DB ends     ********************/

}