package de.reiss.android.losungen.rawdata.insert

import androidx.annotation.RawRes
import androidx.annotation.WorkerThread

interface DatabaseInserter {

    @WorkerThread
    fun insert(language: String, @RawRes rawResId: Int): Boolean

}
