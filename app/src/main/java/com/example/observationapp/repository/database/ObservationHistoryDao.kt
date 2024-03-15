package com.example.observationapp.repository.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.observationapp.models.ObservationHistory
import com.example.observationapp.util.ApplicationDBTables
import com.example.observationapp.util.CommonConstant

@Dao
interface ObservationHistoryDao {

    /*******************    Insert Data into DB Starts     ********************/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObservationHistoryList(list: List<ObservationHistory>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObservationHistory(model: ObservationHistory): Long

    @Query("UPDATE ${ApplicationDBTables.TABLE_OBSERVATION_HISTORY} SET isImagesUpload = :isImagesUpload , observation_image = :observationImage WHERE primaryObservationId = :primaryId")
    suspend fun updateObservationHistory(
        isImagesUpload: Boolean,
        observationImage: List<String>,
        primaryId: Int
    ): Int

    @Query("UPDATE ${ApplicationDBTables.TABLE_OBSERVATION_HISTORY} SET isOffline = :isFormUpload , observation_number = :observationNumber WHERE primaryObservationId = :primaryId")
    suspend fun updateFormObservationHistory(
        isFormUpload: Boolean,
        observationNumber: String,
        primaryId: Int
    ): Int


    /*******************    Insert Data into DB Ends     ********************/


    /*******************    Get Data from DB Starts     ********************/
    /*
    * isImagesUpload ---- is a boolean value
    * isOffline ---- is a boolean value
    *  0 means false --${CommonConstant.FALSE}
    *  1 means true --- ${CommonConstant.TRUE}
    *
    * */

    @Query("SELECT * FROM ${ApplicationDBTables.TABLE_OBSERVATION_HISTORY} WHERE isImagesUpload = ${CommonConstant.FALSE}")
    suspend fun getOfflineObservationHistoryList(): List<ObservationHistory>

    @Query("SELECT * FROM ${ApplicationDBTables.TABLE_OBSERVATION_HISTORY} WHERE isOffline = ${CommonConstant.TRUE}")
    suspend fun getOfflineObservationFormHistoryList(): List<ObservationHistory>

    @Query("SELECT * FROM ${ApplicationDBTables.TABLE_OBSERVATION_HISTORY}")
    suspend fun getObservationHistoryList(): List<ObservationHistory>

    /*******************    Get Data from DB Ends     ********************/

    /*******************    delete Data from DB Starts     ********************/

    @Query("Delete from ${ApplicationDBTables.TABLE_OBSERVATION_HISTORY}")
    suspend fun deleteAllObservationHistory(): Int

    /*******************    delete Data from DB ends     ********************/

}