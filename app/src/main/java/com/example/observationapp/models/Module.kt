package com.example.observationapp.models

data class Module(
    val module_id: String,
    val module_name: String
) {
    override fun toString(): String {
        return "Module(module_id='$module_id', module_name='$module_name')"
    }
}