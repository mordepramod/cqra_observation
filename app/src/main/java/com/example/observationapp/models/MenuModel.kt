package com.example.observationapp.models

data class MenuModel(
    val module: Module,
    val submodules: List<Submodule>
) {
    override fun toString(): String {
        return "MenuModel(module=$module, submodules=$submodules)"
    }
}