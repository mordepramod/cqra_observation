package com.example.observationapp.repository.database

import androidx.room.TypeConverter
import com.example.observationapp.models.Accountable
import com.example.observationapp.models.Module
import com.example.observationapp.models.ObservationCategory
import com.example.observationapp.models.ObservationHistory
import com.example.observationapp.models.ObservationSeverity
import com.example.observationapp.models.ObservationType
import com.example.observationapp.models.StageModel
import com.example.observationapp.models.StructureModel
import com.example.observationapp.models.SubUnitModel
import com.example.observationapp.models.Submodule
import com.example.observationapp.models.TradeGroupModel
import com.example.observationapp.models.TradeModel
import com.example.observationapp.models.UnitModel
import com.example.observationapp.models.UserModel
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

    @TypeConverter
    fun toAccountableList(value: String): List<Accountable>? {
        val listType = object : TypeToken<List<Accountable>>() {}.type
        return Gson().fromJson<List<Accountable>>(value, listType)
    }

    @TypeConverter
    fun fromAccountableList(value: List<Accountable>): String {
        val listType = object : TypeToken<List<Accountable>>() {}.type
        return Gson().toJson(value, listType)
    }

    @TypeConverter
    fun toObservationCategoryList(value: String): List<ObservationCategory>? {
        val listType = object : TypeToken<List<ObservationCategory>>() {}.type
        return Gson().fromJson<List<ObservationCategory>>(value, listType)
    }

    @TypeConverter
    fun fromObservationCategoryList(value: List<ObservationCategory>): String {
        val listType = object : TypeToken<List<ObservationCategory>>() {}.type
        return Gson().toJson(value, listType)
    }

    @TypeConverter
    fun toObservationSeverityList(value: String): List<ObservationSeverity>? {
        val listType = object : TypeToken<List<ObservationSeverity>>() {}.type
        return Gson().fromJson<List<ObservationSeverity>>(value, listType)
    }

    @TypeConverter
    fun fromObservationSeverityList(value: List<ObservationSeverity>): String {
        val listType = object : TypeToken<List<ObservationSeverity>>() {}.type
        return Gson().toJson(value, listType)
    }

    @TypeConverter
    fun toObservationTypeList(value: String): List<ObservationType>? {
        val listType = object : TypeToken<List<ObservationType>>() {}.type
        return Gson().fromJson<List<ObservationType>>(value, listType)
    }

    @TypeConverter
    fun fromObservationTypeList(value: List<ObservationType>): String {
        val listType = object : TypeToken<List<ObservationType>>() {}.type
        return Gson().toJson(value, listType)
    }

    @TypeConverter
    fun toTradeGroupModelList(value: String): List<TradeGroupModel>? {
        val listType = object : TypeToken<List<TradeGroupModel>>() {}.type
        return Gson().fromJson<List<TradeGroupModel>>(value, listType)
    }

    @TypeConverter
    fun fromTradeGroupModelList(value: List<TradeGroupModel>): String {
        val listType = object : TypeToken<List<TradeGroupModel>>() {}.type
        return Gson().toJson(value, listType)
    }

    @TypeConverter
    fun toTradeModelList(value: String): List<TradeModel>? {
        val listType = object : TypeToken<List<TradeModel>>() {}.type
        return Gson().fromJson<List<TradeModel>>(value, listType)
    }

    @TypeConverter
    fun fromTradeModelList(value: List<TradeModel>): String {
        val listType = object : TypeToken<List<TradeModel>>() {}.type
        return Gson().toJson(value, listType)
    }


    @TypeConverter
    fun toUserModel(value: String): List<UserModel>? {
        val listType = object : TypeToken<List<UserModel>>() {}.type
        return Gson().fromJson<List<UserModel>>(value, listType)
    }

    @TypeConverter
    fun fromUserModel(value: List<UserModel>): String {
        val listType = object : TypeToken<List<UserModel>>() {}.type
        return Gson().toJson(value, listType)
    }

    @TypeConverter
    fun toMenuModule(value: String): List<Module>? {
        val listType = object : TypeToken<List<Module>>() {}.type
        return Gson().fromJson<List<Module>>(value, listType)
    }

    @TypeConverter
    fun fromMenuModule(value: List<Module>): String {
        val listType = object : TypeToken<List<Module>>() {}.type
        return Gson().toJson(value, listType)
    }

    @TypeConverter
    fun toMenuSubModule(value: String): List<Submodule>? {
        val listType = object : TypeToken<List<Submodule>>() {}.type
        return Gson().fromJson<List<Submodule>>(value, listType)
    }

    @TypeConverter
    fun fromMenuSubModule(value: List<Submodule>): String {
        val listType = object : TypeToken<List<Submodule>>() {}.type
        return Gson().toJson(value, listType)
    }

    @TypeConverter
    fun toObservationHistory(value: String): List<ObservationHistory>? {
        val listType = object : TypeToken<List<ObservationHistory>>() {}.type
        return Gson().fromJson<List<ObservationHistory>>(value, listType)
    }

    @TypeConverter
    fun fromObservationHistory(value: List<ObservationHistory>): String {
        val listType = object : TypeToken<List<ObservationHistory>>() {}.type
        return Gson().toJson(value, listType)
    }

    @TypeConverter
    fun toFloorList(value: String): List<Int>? {
        val listType = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson<List<Int>>(value, listType)
    }

    @TypeConverter
    fun fromFloorList(value: List<Int>): String {
        val listType = object : TypeToken<List<Int>>() {}.type
        return Gson().toJson(value, listType)
    }

    @TypeConverter
    fun toStringList(value: String): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson<List<String>>(value, listType)
    }

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().toJson(value, listType)
    }


}