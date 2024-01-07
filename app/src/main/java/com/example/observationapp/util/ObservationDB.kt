package com.example.observationapp.util

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.observationapp.models.ProjectModelItem
import com.example.observationapp.models.StageModel
import com.example.observationapp.models.StructureModel
import com.example.observationapp.models.SubUnitModel
import com.example.observationapp.models.UnitModel
import com.example.observationapp.repository.database.ProjectDao
import com.example.observationapp.repository.database.ProjectModelConverter


@Database(
    entities = [ProjectModelItem::class, StructureModel::class,
        StageModel::class, UnitModel::class, SubUnitModel::class], version = 1, exportSchema = false
)
@TypeConverters(ProjectModelConverter::class)
abstract class ObservationDB : RoomDatabase() {
    abstract fun projectDao(): ProjectDao

}