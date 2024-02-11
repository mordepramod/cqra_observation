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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertObservationHistory(model: ObservationHistory): Long

    @Query("UPDATE ${ApplicationDBTables.TABLE_OBSERVATION_HISTORY} SET isImagesUpload = :isImagesUpload WHERE temp_observation_number = :tempId AND primaryObservationId = :primaryId")
    suspend fun updateObservationHistory(
        isImagesUpload: Boolean,
        tempId: String,
        primaryId: Int
    ): Int


    /*******************    Insert Data into DB Ends     ********************/


    /*******************    Get Data from DB Starts     ********************/
    @Query("SELECT * FROM ${ApplicationDBTables.TABLE_OBSERVATION_HISTORY} WHERE isImagesUpload = 0 AND observation_image != \"[]\" ")
    suspend fun getOfflineObservationHistoryList(): List<ObservationHistory>

    @Query("SELECT * FROM ${ApplicationDBTables.TABLE_OBSERVATION_HISTORY}")
    suspend fun getObservationHistoryList(): List<ObservationHistory>

    /*******************    Get Data from DB Ends     ********************/

    /*******************    delete Data from DB Starts     ********************/

    @Query("Delete from ${ApplicationDBTables.TABLE_OBSERVATION_HISTORY}")
    suspend fun deleteAllObservationHistory(): Int

    /*******************    delete Data from DB ends     ********************/

}