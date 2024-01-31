package com.example.observationapp.util

import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
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
        val sdf =
            SimpleDateFormat(CommonConstant.DATE_FORMAT_yyyy_dd_mm_hh_mm_ss, Locale.getDefault())
        return sdf.format(Date())

    }

    fun getSelectedDateInString(timestamp: Long): String {
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = timestamp
        return DateFormat.format(CommonConstant.DATE_FORMAT_yyyy_dd_mm, calendar).toString()

    }

    fun getTimeStampInLong(string: String): Long {
        val formatter = SimpleDateFormat(CommonConstant.DATE_FORMAT_yyyy_dd_mm, Locale.getDefault())
        val cal = Calendar.getInstance()
        cal.time = formatter.parse(string) as Date
        cal.add(Calendar.DATE, 1)
        return cal.time.time
    }

    fun getFilePathWithoutName(filePath: String): String {
        val index = getLastIndexOfFilePath(filePath)
        return filePath.substring(0, index)
    }

    fun getFileNameFromPath(filePath: String): String {
        val index = getLastIndexOfFilePath(filePath)
        return filePath.substring(index, filePath.length)
    }

    fun getLastIndexOfFilePath(filePath: String): Int {
        return filePath.lastIndexOf('/')
    }

}