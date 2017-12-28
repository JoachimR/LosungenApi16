package de.reiss.android.losungen.apk.migrate

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.support.annotation.WorkerThread
import de.reiss.android.losungen.database.NoteItem
import de.reiss.android.losungen.database.NoteItemDao
import de.reiss.android.losungen.model.BibleTextPair
import de.reiss.android.losungen.util.extensions.withZeroDayTime
import de.reiss.android.losungen.xmlparser.dateFromString
import javax.inject.Inject

class DatabaseMigrator @Inject constructor(val context: Context,
                                           val noteItemDao: NoteItemDao) {

    @WorkerThread
    fun migrateNotesDatabase() {
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

    class NotesDbCommunicator(context: Context) : SQLiteOpenHelper(context, "notesdb", null, 99) {

        override fun onCreate(db: SQLiteDatabase) {
            writableDatabase.execSQL(createTableStatement)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        }

        fun cursorForAll(): Cursor = readableDatabase.query("notes", null, null, null, null, null, null)

        private val createTableStatement = "CREATE TABLE notes (  _id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT NOT NULL, language TEXT NOT NULL, text1 TEXT NOT NULL, source1 TEXT NOT NULL, text2 TEXT NOT NULL, source2 TEXT NOT NULL, note TEXT NOT NULL )  ; "

    }

}