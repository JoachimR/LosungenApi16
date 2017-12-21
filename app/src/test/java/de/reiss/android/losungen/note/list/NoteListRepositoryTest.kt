package de.reiss.android.losungen.note.list

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.architecture.AsyncLoadStatus
import de.reiss.android.losungen.database.NoteItemDao
import de.reiss.android.losungen.testutil.TestExecutor
import de.reiss.android.losungen.testutil.blockingObserve
import de.reiss.android.losungen.testutil.sampleNoteItem
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@Suppress("IllegalIdentifier")
class NoteListRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var repository: NoteListRepository

    private val noteItemDao = mock<NoteItemDao>()

    @Before
    fun setUp() {
        repository = NoteListRepository(TestExecutor(), noteItemDao)
    }

    @Test
    fun `when 0 items available then repo returns empty unfiltered list`() {
        whenever(noteItemDao.all()).thenReturn(emptyList())

        val result = loadItemsFromRepo()

        assertEquals(AsyncLoadStatus.SUCCESS, result.loadStatus)
        val data = result.data ?: throw NullPointerException()

        assertEquals(0, data.allItems.size)
    }

    @Test
    fun `when 1 item available then repo returns unfiltered list with 1 item`() {
        checkList(1)
    }

    @Test
    fun `when 99 items available then repo returns unfiltered list with 99 items`() {
        checkList(99)
    }

    private fun checkList(amount: Int) {
        val databaseItems = (1..amount).map { sampleNoteItem(it) }
        whenever(noteItemDao.all()).thenReturn(databaseItems)
        val result = loadItemsFromRepo()
        assertEquals(AsyncLoadStatus.SUCCESS, result.loadStatus)
        val data = result.data ?: throw NullPointerException()
        assertEquals(databaseItems.size, data.allItems.size)
        for ((index, databaseItem) in databaseItems.withIndex()) {

            val note = data.allItems[index]

            assertEquals(databaseItem.date, note.date)
            assertEquals(databaseItem.note, note.noteText)
            assertEquals(databaseItem.text1, note.losungContent.text1)
            assertEquals(databaseItem.source1, note.losungContent.source1)
            assertEquals(databaseItem.text2, note.losungContent.text2)
            assertEquals(databaseItem.source2, note.losungContent.source2)
        }
    }

    private fun loadItemsFromRepo(): AsyncLoad<FilteredNotes> {
        val liveData = MutableLiveData<AsyncLoad<FilteredNotes>>()
        liveData.value = AsyncLoad.success(FilteredNotes())
        repository.getAllNotes(result = liveData)
        return liveData.blockingObserve() ?: throw NullPointerException()
    }

}