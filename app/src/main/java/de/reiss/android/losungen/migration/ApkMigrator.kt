package de.reiss.android.losungen.migration

import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Color
import android.support.annotation.WorkerThread
import de.reiss.android.losungen.R
import de.reiss.android.losungen.allLanguages
import de.reiss.android.losungen.database.*
import de.reiss.android.losungen.logger.logErrorWithCrashlytics
import de.reiss.android.losungen.model.BibleTextPair
import de.reiss.android.losungen.preferences.AppPreferences
import de.reiss.android.losungen.rawdata.RawToDatabaseWriter
import de.reiss.android.losungen.util.extensions.withZeroDayTime
import de.reiss.android.losungen.xmlparser.dateFromString
import java.util.concurrent.Executor
import javax.inject.Inject

class ApkMigrator @Inject constructor(val context: Context,
                                      val executor: Executor,
                                      val noteItemDao: NoteItemDao,
                                      val dailyLosungItemDao: DailyLosungItemDao,
                                      val languageItemDao: LanguageItemDao,
                                      val appPreferences: AppPreferences,
                                      val rawToDatabaseWriter: RawToDatabaseWriter) {

    companion object {


        const val initKey = "PERFORMED_INIT";

        const val migrateKey = "PERFORMED_MIGRATION_TO_1001";

    }

    fun initializeIfNeeded() {
        val preferences = appPreferences.preferences

        try {
            val alreadyInitialized = preferences.getBoolean(initKey, false)
            if (alreadyInitialized.not()) {
                preferences.edit().putBoolean(initKey, true).apply()

                initDatabase()
            }
        } catch (e: Exception) {
            logErrorWithCrashlytics(e) {
                "error when trying to init app"
            }
            preferences.edit().putBoolean(initKey, false).apply()
        }
    }

    private fun initDatabase() {
        executor.execute {
            try {
                val items = allLanguages.map {
                    LanguageItem(it.key, it.name, it.languageCode)
                }
                languageItemDao.insertAll(*items.toTypedArray())
            } catch (e: Exception) {
                logErrorWithCrashlytics(e) {
                    "error when trying to init databases"
                }
            }
        }
    }

    fun migrateIfNeeded() {
        val preferences = appPreferences.preferences

        try {
            val alreadyMigrated = preferences.getBoolean(migrateKey, false)
            if (alreadyMigrated.not()) {
                preferences.edit().putBoolean(migrateKey, true).apply()

                migrateAppPreferences(preferences)
                migrateWidgetPreferences(preferences)
                cleanOldPreferences(preferences)
                migrateDatabases()
            }
        } catch (e: Exception) {
            logErrorWithCrashlytics(e) {
                "error when trying to migrate preferences to version app 126"
            }
        }
    }

    private fun migrateDatabases() {
        executor.execute {
            try {
                migrateNotesDatabase()

            } catch (e: Exception) {
                logErrorWithCrashlytics(e) {
                    "error when trying to add new content to database"
                }
            }
        }
    }

    @WorkerThread
    private fun migrateNotesDatabase() {
        val notesDbCommunicator = NotesDbCommunicator(context)
        val cursor = notesDbCommunicator.cursorForAll()
        if (cursor.count > 0) {
            while (cursor.moveToNext()) {
                moveToNewDatabase(cursor)
            }
        }
    }

    private fun moveToNewDatabase(cursor: Cursor) {
        // find date of old note
        cursor.getString(cursor.getColumnIndex("date"))?.let { dateString ->
            dateFromString(dateString)?.let { date ->

                // only insert old note into new database if there is not already
                // a note in the new database for this date
                val item = noteItemDao.byDate(date)
                if (item == null) {
                    noteItemDao.insertOrReplace(
                            NoteItem(date.withZeroDayTime(),
                                    BibleTextPair(
                                            cursor.getString(cursor.getColumnIndex("text1")),
                                            cursor.getString(cursor.getColumnIndex("source1")),
                                            cursor.getString(cursor.getColumnIndex("text2")),
                                            cursor.getString(cursor.getColumnIndex("source2"))),
                                    cursor.getString(cursor.getColumnIndex("note")))
                    )
                }
            }
        }
    }

    private fun cleanOldPreferences(preferences: SharedPreferences) {
        val edit = preferences.edit()
        preferences.all
                .map { it.key }
                .filter { keysAfterMigration.contains(it).not() }
                .forEach {
                    edit.remove(it)
                }
        edit.apply()
    }

    private fun migrateAppPreferences(preferences: SharedPreferences) {
        val edit = preferences.edit()

        preferences.getString("pref_theme_key", null)?.let {
            if (context.resources.getStringArray(R.array.pref_theme_themes_values).contains(it)) {
                edit.putString(context.getString(R.string.pref_theme_key), it)
            }
        }

        preferences.getBoolean("pref_shownotes_key", true).let {
            edit.putBoolean(context.getString(R.string.pref_show_notes_key), it)
        }

        preferences.getString("pref_fontsize_key", null)?.let {
            try {
                edit.putInt(context.getString(R.string.pref_fontsize_key), Integer.parseInt(it))
            } catch (e: Exception) {
                logErrorWithCrashlytics(e) { "error when trying to migrate app font size" }
            }
        }

        edit.apply()
    }

    private fun migrateWidgetPreferences(preferences: SharedPreferences) {
        val edit = preferences.edit()

        preferences.getString("pref_widget_fontsize_key", null)?.let {
            try {
                edit.putInt(context.getString(R.string.pref_widget_fontsize_key),
                        Integer.parseInt(it))
            } catch (e: Exception) {
                logErrorWithCrashlytics(e) { "error when trying to migrate widget font size" }
            }
        }

        preferences.getString("pref_widget_fontcolor_key", null)?.let {
            try {
                val color = Color.parseColor(it)
                edit.putInt(context.getString(R.string.pref_widget_fontcolor_key), color)
            } catch (e: Exception) {
                logErrorWithCrashlytics(e) { "error when trying to migrate widget font color" }
            }
        }

        preferences.getString("pref_widget_backgroundcolor_key", null)?.let {
            edit.putString(context.getString(R.string.pref_widget_backgroundcolor_key),
                    if (it == "border_content_transparent") {
                        context.getString(R.string.pref_widget_backgroundcolor_default)
                    } else {
                        it
                    })
        }

        preferences.getBoolean("pref_widget_centered_text_key", true).let {
            edit.putBoolean(context.getString(R.string.pref_widget_centered_text_key), it)
        }

        preferences.getBoolean("pref_widget_showdate_key", true).let {
            edit.putBoolean(context.getString(R.string.pref_widget_showdate_key), it)
        }

        edit.apply()
    }

    private val keysAfterMigration: List<String> by lazy {
        listOf<String>(
                migrateKey,
                context.getString(R.string.pref_theme_key),
                context.getString(R.string.pref_language_key),
                context.getString(R.string.pref_show_notes_key),
                context.getString(R.string.pref_fontsize_key),
                context.getString(R.string.pref_fontcolor_key),
                context.getString(R.string.pref_backgroundcolor_key),
                context.getString(R.string.pref_show_toolbar_key),
                context.getString(R.string.pref_show_cards_key),
                context.getString(R.string.pref_cardbackgroundcolor_key),
                context.getString(R.string.pref_widget_backgroundcolor_key),
                context.getString(R.string.pref_widget_fontcolor_key),
                context.getString(R.string.pref_widget_fontsize_key),
                context.getString(R.string.pref_widget_showdate_key),
                context.getString(R.string.pref_widget_centered_text_key),
                context.getString(R.string.pref_show_daily_notification_key)
        )
    }

    class NotesDbCommunicator(context: Context) : SQLiteOpenHelper(context, "notesdb", null, 2) {

        override fun onCreate(db: SQLiteDatabase) {
            writableDatabase.execSQL(createTableStatement)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        }

        fun cursorForAll(): Cursor = readableDatabase.query("notes", null, null, null, null, null, null)

        private val createTableStatement = "CREATE TABLE notes (  _id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT NOT NULL, language TEXT NOT NULL, text1 TEXT NOT NULL, source1 TEXT NOT NULL, text2 TEXT NOT NULL, source2 TEXT NOT NULL, note TEXT NOT NULL )  ; "

    }

}