package de.reiss.android.losungen.note.export

import android.os.Environment
import de.reiss.android.losungen.logger.logWarn
import de.reiss.android.losungen.util.extensions.asDateString
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

open class FileProvider {

    private val directoryName = "LosungNotes"

    val fileName = "${Date().asDateString()}_losungnotes.xml"

    open val directory by lazy {
        Environment.getExternalStorageDirectory().toString() +
                File.separator + directoryName
    }

    open fun isExternalStorageWritable() =
        Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()

    open fun createBufferedOutputStream(): BufferedOutputStream? {
        try {
            return BufferedOutputStream(FileOutputStream(createFile()))
        } catch (e: IOException) {
            logWarn(e) { "error when creating file" }
        } catch (e: SecurityException) {
            logWarn(e) { "security error when creating file" }
        }
        return null
    }

    private fun createFile(): File {
        val dir = File(directory)
        dir.mkdirs()
        val file = File(dir, fileName)
        file.createNewFile()
        return file
    }

}