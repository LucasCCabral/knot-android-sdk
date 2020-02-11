package com.cesar.knot_sdk

import android.util.Log

object LogWrapper {

    private const val TAG = "KNOT-SDK"
    private var logEnable = false

    enum class LogLevel {
        VERBOSE,
        DEBUG,
        INFO,
        WARN,
        ERROR
    }

    fun log(msg : String?, logPriority : LogLevel = LogLevel.DEBUG) {

        if (logEnable) when (logPriority) {
            LogLevel.VERBOSE -> Log.v(TAG, msg)
            LogLevel.DEBUG   -> Log.d(TAG, msg)
            LogLevel.INFO    -> Log.i(TAG, msg)
            LogLevel.WARN    -> Log.w(TAG, msg)
            LogLevel.ERROR   -> Log.e(TAG, msg)
        }
    }
}
