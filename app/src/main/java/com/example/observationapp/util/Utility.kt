package com.example.observationapp.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.text.format.DateFormat
import android.util.Log
import com.example.observationapp.BuildConfig
import com.google.gson.JsonArray
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
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

    private fun getFileNameFromPath(filePath: String): String {
        val index = getLastIndexOfFilePath(filePath) + 1
        return filePath.substring(index, filePath.length)
    }

    private fun getLastIndexOfFilePath(filePath: String): Int {
        return filePath.lastIndexOf('/')
    }

    private fun getFileNameWithoutExtension(filePath: String): String {
        val index = getLastIndexOfFilePathWithDot(filePath)
        return filePath.substring(0, index)
    }

    private fun getLastIndexOfFilePathWithDot(filePath: String): Int {
        return filePath.lastIndexOf('.')
    }

    fun openAppSettings(context: Context) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts(
            "package",
            BuildConfig.APPLICATION_ID, null
        )
        intent.data = uri
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun prepareFilePart(filePathList: List<String>?): List<MultipartBody.Part> {
        val list = arrayListOf<MultipartBody.Part>()
        filePathList?.forEach {
            val file = File(it)
            val requestBody = file.asRequestBody(CommonConstant.MULTIPART.toMediaTypeOrNull())
            val multipart =
                MultipartBody.Part.createFormData("observation_image", file.name, requestBody)
            list.add(multipart)
        }
        return list
    }

    fun customisedImageList(
        savedPathList: List<String>,
        projectId: String,
        structureId: String,
        tradeId: String,
        tag: String = ""
    ): JsonArray {
        val jsonArray = JsonArray()
        savedPathList.forEachIndexed { index, path ->
            val fileNameWithExtension = getFileNameFromPath(path)
            val fileName = getFileNameWithoutExtension(fileNameWithExtension)
            Log.d(
                tag,
                "customisedImageList: path: $path, fileNameWithExtension: $fileNameWithExtension, fileName: $fileName"
            )
            val getFileName =
                "${projectId}_${structureId}_${tradeId}_${fileName}_${index + 1}${CommonConstant.FILE_EXTENSIONS}"
            jsonArray.add(getFileName)
        }
        return jsonArray
    }
}