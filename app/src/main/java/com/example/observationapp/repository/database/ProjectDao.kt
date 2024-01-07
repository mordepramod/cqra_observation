package com.example.observationapp.repository.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.observationapp.models.ProjectModelItem
import com.example.observationapp.models.StageModel
import com.example.observationapp.models.StructureModel
import com.example.observationapp.models.SubUnitModel
import com.example.observationapp.models.UnitModel
import com.example.observationapp.util.ApplicationDBTables

@Dao
interface ProjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjectList(list: List<ProjectModelItem>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStructureList(list: List<StructureModel>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveStageModelsList(list: List<StageModel>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUnitModelsList(list: List<UnitModel>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSubUnitModelsList(list: List<SubUnitModel>): List<Long>

    @Query("Delete from ${ApplicationDBTables.TABLE_PROJECT}")
    suspend fun deleteAll()

    @Query("SELECT * FROM ${ApplicationDBTables.TABLE_PROJECT}")
    fun getAllProjectList(): LiveData<List<ProjectModelItem>>
}