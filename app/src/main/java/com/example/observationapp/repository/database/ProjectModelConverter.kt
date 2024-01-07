package com.example.observationapp.repository.database

import androidx.room.TypeConverter
import com.example.observationapp.models.StageModel
import com.example.observationapp.models.StructureModel
import com.example.observationapp.models.SubUnitModel
import com.example.observationapp.models.UnitModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ProjectModelConverter {

    /*
        @TypeConverter
        fun toStructureModel(value: String): StructureModelList {
            val listType = object : TypeToken<StructureModelList>() {}.type
            return Gson().fromJson(value, listType)
        }

        @TypeConverter
        fun fromStructureModel(value:StructureModelList): String  {
            val gson = Gson()
            val type = object : TypeToken<StructureModelList>() {}.type
            return gson.toJson(value, type)
        }*/
    @TypeConverter
    fun toStructureModelList(value: String): List<StructureModel>? {
        val listType = object : TypeToken<List<StructureModel>>() {}.type
        return Gson().fromJson<List<StructureModel>>(value, listType)
    }

    @TypeConverter
    fun fromStructureModelList(value: List<StructureModel>): String {
        val gson = Gson()
        val type = object : TypeToken<List<StructureModel>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toStageModelList(value: String): List<StageModel>? {
        val listType = object : TypeToken<List<StageModel>>() {}.type
        return Gson().fromJson<List<StageModel>>(value, listType)
    }

    @TypeConverter
    fun fromStageModelList(value: List<StageModel>): String {
        val listType = object : TypeToken<List<StageModel>>() {}.type
        return Gson().toJson(value, listType)
    }

    @TypeConverter
    fun toUnitModelList(value: String): List<UnitModel>? {
        val listType = object : TypeToken<List<UnitModel>>() {}.type
        return Gson().fromJson<List<UnitModel>>(value, listType)
    }

    @TypeConverter
    fun fromUnitModelList(value: List<UnitModel>): String {
        val listType = object : TypeToken<List<UnitModel>>() {}.type
        return Gson().toJson(value, listType)
    }

    @TypeConverter
    fun toSubUnitModelList(value: String): List<SubUnitModel>? {
        val listType = object : TypeToken<List<SubUnitModel>>() {}.type
        return Gson().fromJson<List<SubUnitModel>>(value, listType)
    }

    @TypeConverter
    fun fromSubUnitModelList(value: List<SubUnitModel>): String {
        val listType = object : TypeToken<List<SubUnitModel>>() {}.type
        return Gson().toJson(value, listType)
    }


}