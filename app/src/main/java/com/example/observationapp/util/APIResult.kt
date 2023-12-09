package com.example.observationapp.util

import android.util.Log
import retrofit2.Response
import javax.net.ssl.SSLException
import javax.net.ssl.SSLHandshakeException
import javax.net.ssl.SSLProtocolException


/**
 * wrapper class for API response
 * */
data class APIResult<out T>(var status: Status, val data: T?, var message: String? = "") {
    enum class Status {
        SUCCESS,
        ERROR
    }

    companion object {
        const val TAG = "APIResult"
        fun <T> success(data: T): APIResult<T> {
            return APIResult(Status.SUCCESS, data, null)
        }

        fun <T> error(
            message: String,
            response: Response<T>? = null,
            exception: Exception?,
        ): APIResult<T> {
            if (response != null) {
                /*var errorModel: ErrorModel? = null
                if (response.code() == 401) {
                    errorModel = Gson().fromJson(response.errorBody()?.charStream(), ErrorModel::class.java)
                }*/
                // error code for Hard coded msg in string
                return APIResult(Status.ERROR, response.body(), "API failed")
            }
            if (exception is SSLException
                || exception is SSLHandshakeException
                || exception is SSLProtocolException
            ) {
                return APIResult(Status.ERROR, null, exception.message)
            } else if (exception is Exception) {
                Log.e(TAG, "error: ", exception)
                return APIResult(Status.ERROR, null, exception.javaClass.simpleName)
            }
            return APIResult(Status.ERROR, null, message)
        }

    }
}