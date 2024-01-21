package com.example.observationapp.util

import android.content.Context
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object Utility {
    inline fun <reified T : Any> Context.launchActivity(
        noinline bundle: Intent.() -> Unit = {}
    ) {
        val intent = createIntent<T>(this)
        intent.bundle()
        startActivity(intent)
    }

    inline fun <reified T : Any> createIntent(context: Context) = Intent(context, T::class.java)

    fun getTodayDateAndTime(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault())
        return sdf.format(Date())

    }

}