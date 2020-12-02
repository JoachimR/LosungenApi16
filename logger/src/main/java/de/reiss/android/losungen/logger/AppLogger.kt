package de.reiss.android.losungen.logger

import android.util.Log

private val TAG = "Losungen"

fun logVerbose(message: () -> String) {
    Log.v(TAG, message())
}

fun logDebug(message: () -> String) {
    Log.d(TAG, message())
}

fun logInfo(message: () -> String) {
    Log.i(TAG, message())
}

fun logWarn(throwable: Throwable? = null, message: () -> String) {
    Log.w(TAG, message(), throwable)
}

fun logError(throwable: Throwable? = null, message: () -> String) {
    Log.e(TAG, message(), throwable)
}
