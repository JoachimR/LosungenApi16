package de.reiss.android.losungen.apk.prepare

import android.content.Context
import androidx.annotation.WorkerThread
import de.reiss.android.losungen.allLanguages
import de.reiss.android.losungen.database.LanguageItem
import de.reiss.android.losungen.database.LanguageItemDao
import de.reiss.android.losungen.logger.logError
import de.reiss.android.losungen.preferences.AppPreferences
import javax.inject.Inject

class ApkPrepare @Inject constructor(val context: Context,
                                     val appPreferences: AppPreferences,
                                     val languageItemDao: LanguageItemDao) {
    companion object {

        const val initKey = "PERFORMED_INIT"

    }

    @WorkerThread
    fun prepareApkIfNeeded() {
        val preferences = appPreferences.preferences

        try {
            val alreadyInitialized = preferences.getBoolean(initKey, false)
            if (alreadyInitialized.not()) {
                preferences.edit().putBoolean(initKey, true).apply()

                initDatabase()
            }
        } catch (e: Exception) {
            logError(e) {
                "error when trying to init app"
            }
            preferences.edit().putBoolean(initKey, false).apply()
        }
    }

    @WorkerThread
    private fun initDatabase() {
        try {
            allLanguages.map {
                languageItemDao.insert(LanguageItem(it.key, it.name, it.languageCode))
            }
        } catch (e: Exception) {
            logError(e) {
                "error when trying to init database"
            }
        }
    }

}