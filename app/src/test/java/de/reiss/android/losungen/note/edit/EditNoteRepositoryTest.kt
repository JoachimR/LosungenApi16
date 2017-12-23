package de.reiss.android.losungen.note.edit

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.nhaarman.mockito_kotlin.*
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.architecture.AsyncLoadStatus
import de.reiss.android.losungen.database.NoteItemDao
import de.reiss.android.losungen.model.Note
import de.reiss.android.losungen.testutil.TestExecutor
import de.reiss.android.losungen.testutil.bibleTextPair
import de.reiss.android.losungen.testutil.blockingObserve
import de.reiss.android.losungen.testutil.sampleNoteItem
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.RuntimeException
import java.util.*

@Suppress("IllegalIdentifier")
class EditNoteRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var repository: EditNoteRepository

    private val noteItemDao = mock<NoteItemDao>()

    @Before
    fun setUp() {
        repository = EditNoteRepository(TestExecutor(), noteItemDao)
    }

    @Test
    fun `given no note stored yet and new note has some text then repo inserts new note`() {
        whenever(noteItemDao.byDate(any())).thenReturn(null)

        doInsertOrUpdateNote(text = "some text")

        verify(noteItemDao, never()).delete(any())
        verify(noteItemDao).insertOrReplace(any())
    }

    @Test
    fun `given some note stored yet and new note has some text then repo replaces note`() {
        whenever(noteItemDao.byDate(any())).thenReturn(sampleNoteItem(0))

        doInsertOrUpdateNote(text = "some text")

        verify(noteItemDao, never()).delete(any())
        verify(noteItemDao).insertOrReplace(any())
    }

    @Test
    fun `given no note stored yet and new note has no text nothing happens`() {
        whenever(noteItemDao.byDate(any())).thenReturn(null)

        doInsertOrUpdateNote(text = "")

        verify(noteItemDao, never()).delete(any())
        verify(noteItemDao, never()).insertOrReplace(any())
    }

    @Test
    fun `given some note stored yet and new note has no text then repo deletes note`() {
        whenever(noteItemDao.byDate(any())).thenReturn(sampleNoteItem(0))

        doInsertOrUpdateNote(text = "")

        verify(noteItemDao).delete(any())
        verify(noteItemDao, never()).insertOrReplace(any())
    }

    @Test
    fun `when exception during loading of a note then repository returns error`() {
        whenever(noteItemDao.byDate(any())).then {
            throw RuntimeException("some exception occurred")
        }

        val liveData = MutableLiveData<AsyncLoad<Note?>>()
        repository.loadNote(Date(), liveData)
        val result = liveData.blockingObserve() ?: throw NullPointerException()

        assertEquals(AsyncLoadStatus.ERROR, result.loadStatus)
    }

    @Test
    fun `when exception during storing of a note then repository returns error`() {
        whenever(noteItemDao.insertOrReplace(any())).then {
            throw RuntimeException("some exception occurred")
        }

        val result = doInsertOrUpdateNote("some text")

        assertEquals(AsyncLoadStatus.ERROR, result.loadStatus)
    }

    private fun doInsertOrUpdateNote(text: String): AsyncLoad<Void> {
        val liveData = MutableLiveData<AsyncLoad<Void>>()
        repository.updateNote(Date(), text, bibleTextPair(0), liveData)
        return liveData.blockingObserve() ?: throw NullPointerException()
    }

}