package de.reiss.android.losungen.note.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockito_kotlin.*
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.architecture.AsyncLoadStatus
import de.reiss.android.losungen.database.NoteItemDao
import de.reiss.android.losungen.database.converter.Converter
import de.reiss.android.losungen.model.Note
import de.reiss.android.losungen.testutil.TestExecutor
import de.reiss.android.losungen.testutil.blockingObserve
import de.reiss.android.losungen.testutil.sampleNoteItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@Suppress("IllegalIdentifier")
class NoteDetailsRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var repository: NoteDetailsRepository

    private val noteItemDao = mock<NoteItemDao>()

    private val noteItem = sampleNoteItem(0)
    private val note = Converter.itemToNote(noteItem)!!

    @Before
    fun setUp() {
        repository = NoteDetailsRepository(TestExecutor(), noteItemDao)
    }

    @Test
    fun `when load note requested and note is not available then error is returned`() {
        whenever(noteItemDao.byDate(any())).thenReturn(null)

        val result = loadNote()

        verify(noteItemDao).byDate(eq(noteItem.date.withZeroDayTime()))
        assertEquals(AsyncLoadStatus.ERROR, result.loadStatus)
        assertNull(result.data)
    }

    @Test
    fun `when load note requested and note is available then the note is returned`() {
        whenever(noteItemDao.byDate(any())).thenReturn(noteItem)

        val result = loadNote()

        verify(noteItemDao).byDate(eq(noteItem.date.withZeroDayTime()))
        assertEquals(AsyncLoadStatus.SUCCESS, result.loadStatus)
        assertEquals(note, result.data)
    }

    @Test
    fun `when delete note requested and note is not available then error is returned`() {
        whenever(noteItemDao.byDate(any())).thenReturn(noteItem)
        whenever(noteItemDao.delete(any())).thenReturn(0)

        val result = deleteNote()

        verify(noteItemDao).delete(eq(noteItem))
        assertEquals(AsyncLoadStatus.ERROR, result.loadStatus)
    }

    @Test
    fun `when delete note requested and note is not available then success is returned`() {
        whenever(noteItemDao.byDate(any())).thenReturn(noteItem)
        whenever(noteItemDao.delete(any())).thenReturn(1)

        val result = deleteNote()

        verify(noteItemDao).delete(eq(noteItem))
        assertEquals(AsyncLoadStatus.SUCCESS, result.loadStatus)
    }

    private fun loadNote(): AsyncLoad<Note> {
        val liveData = MutableLiveData<AsyncLoad<Note>>()
        repository.loadNote(note, liveData)
        return liveData.blockingObserve() ?: throw NullPointerException()
    }

    private fun deleteNote(): AsyncLoad<Void> {
        val liveData = MutableLiveData<AsyncLoad<Void>>()
        repository.deleteNote(note, liveData)
        return liveData.blockingObserve() ?: throw NullPointerException()
    }

}