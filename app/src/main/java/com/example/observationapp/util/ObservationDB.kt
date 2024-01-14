package com.example.observationapp.util

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.observationapp.models.Accountable
import com.example.observationapp.models.Module
import com.example.observationapp.models.ObservationCategory
import com.example.observationapp.models.ObservationSeverity
import com.example.observationapp.models.ObservationType
import com.example.observationapp.models.ProjectModelItem
import com.example.observationapp.models.StageModel
import com.example.observationapp.models.StructureModel
import com.example.observationapp.models.SubUnitModel
import com.example.observationapp.models.Submodule
import com.example.observationapp.models.TradeGroupModel
import com.example.observationapp.models.TradeModel
import com.example.observationapp.models.UnitModel
import com.example.observationapp.models.UserModel
import com.example.observationapp.repository.database.LoginDao
import com.example.observationapp.repository.database.ObservationDao
import com.example.observationapp.repository.database.ProjectDao
import com.example.observationapp.repository.database.ProjectModelConverter

@Database(
    entities = [
        ProjectModelItem::class,
        StructureModel::class,
        StageModel::class,
        UnitModel::class,
        SubUnitModel::class,
        Accountable::class,
        ObservationCategory::class,
        ObservationSeverity::class,
        ObservationType::class,
        TradeGroupModel::class,
        TradeModel::class,
        UserModel::class,
        Module::class,
        Submodule::class,
    ], version = 1, exportSchema = false
)
@TypeConverters(ProjectModelConverter::class)
abstract class ObservationDB : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun observationDao(): ObservationDao
    abstract fun loginDao(): LoginDao

}