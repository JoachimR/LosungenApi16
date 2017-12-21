package de.reiss.android.losungen.main.content

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.architecture.AsyncLoadStatus.ERROR
import de.reiss.android.losungen.architecture.AsyncLoadStatus.SUCCESS
import de.reiss.android.losungen.database.DailyLosungItemDao
import de.reiss.android.losungen.database.LanguageItem
import de.reiss.android.losungen.database.LanguageItemDao
import de.reiss.android.losungen.database.NoteItemDao
import de.reiss.android.losungen.model.DailyLosung
import de.reiss.android.losungen.model.Note
import de.reiss.android.losungen.preferences.AppPreferences
import de.reiss.android.losungen.testutil.TestExecutor
import de.reiss.android.losungen.testutil.blockingObserve
import de.reiss.android.losungen.testutil.sampleDailyLosungItem
import de.reiss.android.losungen.testutil.sampleNoteItem
import de.reiss.android.losungen.util.extensions.withZeroDayTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@Suppress("IllegalIdentifier")
class LosungRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var repository: LosungRepository

    private val dailyLosungItemDao: DailyLosungItemDao = mock()
    private val languageItemDao: LanguageItemDao = mock()
    private val noteItemDao: NoteItemDao = mock()
    private val appPreferences: AppPreferences = mock()

    private val languageItem = LanguageItem("testLanguage", "testLanguageName", "testLanguageCode")
            .apply { id = 1 }

    private val date = Date().withZeroDayTime()

    @Before
    fun setUp() {
        whenever(appPreferences.chosenLanguage).thenReturn(languageItem.language)
        whenever(languageItemDao.find(any())).thenReturn(languageItem)

        repository = LosungRepository(
                executor = TestExecutor(),
                dailyLosungItemDao = dailyLosungItemDao,
                languageItemDao = languageItemDao,
                noteItemDao = noteItemDao,
                appPreferences = appPreferences)
    }

    @Test
    fun `when losung not found return error with message`() {
        whenever(dailyLosungItemDao.byDate(any(), any()))
                .thenReturn(null)

        val result = loadLosungFromRepo()

        assertEquals(ERROR, result.loadStatus)
        assertEquals("Content not found", result.message)
    }

    @Test
    fun `when losung found return losung`() {
        val item = sampleDailyLosungItem(number = 0, languageId = languageItem.id)

        whenever(dailyLosungItemDao.byDate(any(), any()))
                .thenReturn(item)

        val result = loadLosungFromRepo()

        assertEquals(SUCCESS, result.loadStatus)
        val data = result.data ?: throw NullPointerException()

        assertEquals(languageItem.language, data.language)
        assertEquals(date, data.date)
        assertEquals(item.text1, data.content.text1)
        assertEquals(item.source1, data.content.source1)
        assertEquals(item.text2, data.content.text2)
        assertEquals(item.source2, data.content.source2)

    }

    @Test
    fun `when note not found return null`() {
        whenever(noteItemDao.byDate(any()))
                .thenReturn(null)

        val result = loadNoteFromRepo()

        assertEquals(SUCCESS, result.loadStatus)
        assertNull(result.data)
    }

    @Test
    fun `when note found return note`() {
        val item = sampleNoteItem(number = 0)

        whenever(noteItemDao.byDate(any()))
                .thenReturn(item)

        val result = loadNoteFromRepo()

        assertEquals(SUCCESS, result.loadStatus)
        val data = result.data ?: throw NullPointerException()

        assertEquals(date, data.date)
        assertEquals(item.note, data.noteText)
        assertEquals(item.text1, data.losungContent.text1)
        assertEquals(item.source1, data.losungContent.source1)
        assertEquals(item.text2, data.losungContent.text2)
        assertEquals(item.source2, data.losungContent.source2)
    }

    private fun loadLosungFromRepo(): AsyncLoad<DailyLosung> {
        val liveData = MutableLiveData<AsyncLoad<DailyLosung>>()
        repository.getLosungFor(date, liveData)
        return liveData.blockingObserve() ?: throw NullPointerException()
    }

    private fun loadNoteFromRepo(): AsyncLoad<Note> {
        val liveData = MutableLiveData<AsyncLoad<Note>>()
        repository.getNoteFor(date, liveData)
        return liveData.blockingObserve() ?: throw NullPointerException()
    }

}