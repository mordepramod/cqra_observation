package com.example.observationapp.models

data class Submodule(
    val submodule_id: String,
    val submodule_name: String
) {
    override fun toString(): String {
        return "Submodule(submodule_id='$submodule_id', submodule_name='$submodule_name')"
    }
}