package de.reiss.android.losungen.note.export

import android.support.annotation.WorkerThread
import de.reiss.android.losungen.database.NoteItem
import de.reiss.android.losungen.logger.logErrorWithCrashlytics
import de.reiss.android.losungen.util.extensions.asDateString
import java.io.BufferedOutputStream
import java.io.IOException


open class NotesExporter(private val fileProvider: FileProvider) {

    open val fileName = fileProvider.fileName
    open val directory = fileProvider.directory

    @WorkerThread
    open fun exportNotes(notes: List<NoteItem>): Boolean {
        val bos = fileProvider.createBufferedOutputStream() ?: return false
        val exporter = Exporter(bos)
        try {
            doExport(exporter, notes)
            return true
        } catch (e: IOException) {
            logErrorWithCrashlytics(e) { "Error when trying to export notes database" }
        } finally {
            try {
                exporter.close()
            } catch (ignored: IOException) {
            }
        }
        return false
    }

    open fun isExternalStorageWritable() = fileProvider.isExternalStorageWritable()

    private fun doExport(exporter: Exporter, allNotes: List<NoteItem>) {
        exporter.apply {
            startDbExport("NotesDatabase")
            startTable("notes")

            for (note in allNotes) {
                startRow()
                addColumn("date", note.date.asDateString())

                addColumn("text1", note.text1)
                addColumn("source1", note.source1)

                addColumn("text2", note.text2)
                addColumn("source2", note.source2)

                addColumn("note", note.note)
                endRow()
            }
            endTable()
            endDbExport()
        }
    }

    private class Exporter(private val bufferedOutputStream: BufferedOutputStream) {

        companion object {

            private const val CLOSING_WITH_TICK = "'>"
            private const val START_DB = "\n<export-database name='"
            private const val END_DB = "\n</export-database>"
            private const val START_TABLE = "\n\n\n<table name='"
            private const val END_TABLE = "\n</table>"
            private const val START_ROW = "\n\n<row>"
            private const val END_ROW = "\n</row>"
            private const val START_COL = "\n<col name='"
            private const val END_COL = "</col>"

        }

        @Throws(IOException::class)
        fun close() {
            bufferedOutputStream.close()
        }

        @Throws(IOException::class)
        fun startDbExport(dbName: String) {
            val stg = START_DB + dbName + CLOSING_WITH_TICK
            bufferedOutputStream.write(stg.toByteArray())
        }

        @Throws(IOException::class)
        fun endDbExport() {
            bufferedOutputStream.write(END_DB.toByteArray())
        }

        @Throws(IOException::class)
        fun startTable(tableName: String) {
            val stg = START_TABLE + tableName + CLOSING_WITH_TICK
            bufferedOutputStream.write(stg.toByteArray())
        }

        @Throws(IOException::class)
        fun endTable() {
            bufferedOutputStream.write(END_TABLE.toByteArray())
        }

        @Throws(IOException::class)
        fun startRow() {
            bufferedOutputStream.write(START_ROW.toByteArray())
        }

        @Throws(IOException::class)
        fun endRow() {
            bufferedOutputStream.write(END_ROW.toByteArray())
        }

        @Throws(IOException::class)
        fun addColumn(name: String, value: String) {
            val stg = START_COL + name + CLOSING_WITH_TICK + value + END_COL
            bufferedOutputStream.write(stg.toByteArray())
        }

    }

}