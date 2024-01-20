package com.example.observationapp.models

data class LoginResultModel(
    val menus: List<MenuModel>,
    val user: UserModel
) {
    override fun toString(): String {
        return "LoginResultModel(menus=$menus, user=$user)"
    }
}