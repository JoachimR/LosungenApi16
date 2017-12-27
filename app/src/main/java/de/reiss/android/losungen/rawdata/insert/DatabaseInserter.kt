package de.reiss.android.losungen.rawdata.insert

import android.support.annotation.RawRes
import android.support.annotation.WorkerThread

interface DatabaseInserter {

    @WorkerThread
    fun insert(language: String, @RawRes rawResId: Int): Boolean

}
