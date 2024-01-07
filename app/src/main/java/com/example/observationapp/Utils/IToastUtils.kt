package com.example.observationapp.Utils

import androidx.annotation.StringRes

interface IToastUtils {
    fun show(message: String?, type: Int)

    fun show(@StringRes message: Int, type: Int)
}