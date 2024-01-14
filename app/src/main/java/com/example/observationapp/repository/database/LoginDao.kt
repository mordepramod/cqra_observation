package com.example.observationapp.repository.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.observationapp.models.Module
import com.example.observationapp.models.Submodule
import com.example.observationapp.models.UserModel
import com.example.observationapp.util.ApplicationDBTables

@Dao
interface LoginDao {

    /*******************    Insert Data into DB Starts     ********************/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoginData(userModel: UserModel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMenuModel(menuModel: Module): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubModuleList(list: List<Submodule>): List<Long>

    /*******************    Insert Data into DB Ends     ********************/


    /*******************    Get Data from DB Starts     ********************/
    @Query("SELECT * FROM ${ApplicationDBTables.TABLE_MENU_MODULE}")
    suspend fun getModuleData(): List<Module>

    @Query("SELECT * FROM ${ApplicationDBTables.TABLE_USER}")
    suspend fun getLoginUserInfo(): UserModel

    @Query("SELECT * FROM ${ApplicationDBTables.TABLE_MENU_SUBMODULE}")
    fun getMenuSubModuleList(): LiveData<List<Submodule>>

    /*******************    Get Data from DB Ends     ********************/

    /*******************    delete Data from DB Starts     ********************/

    @Query("Delete from ${ApplicationDBTables.TABLE_USER}")
    suspend fun deleteLoggedInUser(): Int

    @Query("Delete from ${ApplicationDBTables.TABLE_MENU_MODULE}")
    suspend fun deleteMenuModule(): Int

    @Query("Delete from ${ApplicationDBTables.TABLE_MENU_SUBMODULE}")
    suspend fun deleteAllMenuSubModuleList(): Int

    /*******************    delete Data from DB ends     ********************/

}