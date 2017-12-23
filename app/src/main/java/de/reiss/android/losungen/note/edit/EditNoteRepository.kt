package de.reiss.android.losungen.note.edit

import android.arch.lifecycle.MutableLiveData
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.database.NoteItem
import de.reiss.android.losungen.database.NoteItemDao
import de.reiss.android.losungen.database.converter.Converter
import de.reiss.android.losungen.logger.logWarn
import de.reiss.android.losungen.model.BibleTextPair
import de.reiss.android.losungen.model.Note
import de.reiss.android.losungen.util.extensions.withZeroDayTime
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

open class EditNoteRepository @Inject constructor(private val executor: Executor,
                                                  private val noteItemDao: NoteItemDao) {

    open fun loadNote(date: Date, result: MutableLiveData<AsyncLoad<Note?>>) {
        val oldData = result.value?.data
        result.postValue(AsyncLoad.loading(oldData))
        executor.execute {
            try {
                val noteItem = noteItemDao.byDate(date.withZeroDayTime())
                val note = Converter.itemToNote(noteItem) // can be null (new note)
                result.postValue(AsyncLoad.success(note))
            } catch (e: Exception) {
                logWarn(e) { "Error while loading note" }
                result.postValue(AsyncLoad.error(oldData))
            }
        }
    }

    open fun updateNote(date: Date,
                        text: String,
                        bibleTextPair: BibleTextPair,
                        result: MutableLiveData<AsyncLoad<Void>>) {
        result.postValue(AsyncLoad.loading())
        executor.execute {
            try {
                if (text.trim().isEmpty()) {
                    noteItemDao.byDate(date.withZeroDayTime())?.let { noteItem ->
                        noteItemDao.delete(noteItem)
                    }
                } else {
                    noteItemDao.insertOrReplace(NoteItem(date.withZeroDayTime(), bibleTextPair, text))
                }
                result.postValue(AsyncLoad.success())
            } catch (e: Exception) {
                logWarn(e) { "Error while trying to update note" }
                result.postValue(AsyncLoad.error())
            }
        }
    }

}