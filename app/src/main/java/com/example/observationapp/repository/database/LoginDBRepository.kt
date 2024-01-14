package com.example.observationapp.repository.database

import androidx.lifecycle.LiveData
import com.example.observationapp.models.Module
import com.example.observationapp.models.Submodule
import com.example.observationapp.models.UserModel
import javax.inject.Inject

class LoginDBRepository @Inject constructor(
    private val loginDao: LoginDao
) {
    suspend fun saveLogin(userModel: UserModel): Long =
        loginDao.insertLoginData(userModel)

    suspend fun saveMenuModule(menuModel: Module): Long =
        loginDao.insertMenuModel(menuModel)

    suspend fun saveMenuSubModuleList(list: List<Submodule>): List<Long> =
        loginDao.insertSubModuleList(list)


    suspend fun deleteLoggedInUser(): Int = loginDao.deleteLoggedInUser()
    suspend fun deleteMenuModule(): Int = loginDao.deleteMenuModule()
    suspend fun deleteAllMenuSubModuleList(): Int = loginDao.deleteAllMenuSubModuleList()

    suspend fun getLoggedInUser(): UserModel {
        return loginDao.getLoginUserInfo()
    }

    fun getModuleData(): LiveData<Module> {
        return loginDao.getModuleData()
    }

    fun getMenuSubModuleList(): LiveData<List<Submodule>> {
        return loginDao.getMenuSubModuleList()
    }
}