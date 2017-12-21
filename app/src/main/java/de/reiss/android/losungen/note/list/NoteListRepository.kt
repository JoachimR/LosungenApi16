package de.reiss.android.losungen.note.list

import android.arch.lifecycle.MutableLiveData
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.database.NoteItemDao
import de.reiss.android.losungen.database.converter.Converter
import de.reiss.android.losungen.model.Note
import java.util.concurrent.Executor
import javax.inject.Inject

open class NoteListRepository @Inject constructor(private val executor: Executor,
                                                  private val noteItemDao: NoteItemDao) {

    open fun getAllNotes(result: MutableLiveData<AsyncLoad<FilteredNotes>>) {
        val oldData = result.value?.data ?: return
        result.postValue(AsyncLoad.loading(oldData))

        executor.execute {
            val query = oldData.query
            val allItems = noteItemDao.all()
                    .mapNotNull {
                        Converter.itemToNote(it)
                    }

            if (query.isEmpty()) {
                result.postValue(AsyncLoad.success(
                        FilteredNotes(
                                allItems = allItems,
                                filteredItems = allItems,
                                query = query)
                ))
            } else {
                val filteredItems = filter(query, allItems)
                result.postValue(AsyncLoad.success(
                        FilteredNotes(
                                allItems = allItems,
                                filteredItems = filteredItems,
                                query = query)
                ))
            }
        }
    }

    open fun applyNewFilter(query: String, result: MutableLiveData<AsyncLoad<FilteredNotes>>) {
        val unfiltered = result.value?.data ?: return
        if (query.isEmpty()) {
            result.postValue(AsyncLoad.success(
                    FilteredNotes(
                            allItems = unfiltered.allItems,
                            filteredItems = unfiltered.allItems,
                            query = query)
            ))
            return
        }

        result.postValue(AsyncLoad.loading(unfiltered))

        executor.execute {
            val filteredItems = filter(query, unfiltered.allItems)

            result.postValue(AsyncLoad.success(
                    FilteredNotes(
                            allItems = unfiltered.allItems,
                            filteredItems = filteredItems,
                            query = query)
            ))
        }


    }

    private fun filter(query: String, noteList: List<Note>) =
            query.toLowerCase().let { match ->
                noteList.filter {
                    it.noteText.toLowerCase().contains(match)
                            || it.losungContent.text1.toLowerCase().contains(match)
                            || it.losungContent.source1.toLowerCase().contains(match)
                            || it.losungContent.text2.toLowerCase().contains(match)
                            || it.losungContent.source2.toLowerCase().contains(match)
                }
            }

}