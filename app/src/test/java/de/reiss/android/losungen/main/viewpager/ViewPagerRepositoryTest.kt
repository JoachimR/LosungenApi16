package de.reiss.android.losungen.main.viewpager

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.nhaarman.mockito_kotlin.*
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.architecture.AsyncLoadStatus
import de.reiss.android.losungen.database.DailyLosungDatabaseItem
import de.reiss.android.losungen.database.DailyLosungItemDao
import de.reiss.android.losungen.database.LanguageItem
import de.reiss.android.losungen.database.LanguageItemDao
import de.reiss.android.losungen.rawdata.RawToDatabaseWriter
import de.reiss.android.losungen.testutil.TestExecutor
import de.reiss.android.losungen.testutil.blockingObserve
import de.reiss.android.losungen.testutil.sampleDailyLosungItem
import de.reiss.android.losungen.util.extensions.firstDayOfYear
import de.reiss.android.losungen.util.extensions.lastDayOfYear
import de.reiss.android.losungen.util.extensions.withZeroDayTime
import de.reiss.android.losungen.widget.WidgetRefresher
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*


@Suppress("IllegalIdentifier")
class ViewPagerRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var repository: ViewPagerRepository

    private val dailyLosungItemDao = mock<DailyLosungItemDao>()
    private val languageItemDao = mock<LanguageItemDao>()

    private val rawToDatabaseWriter = mock<RawToDatabaseWriter>()
    private val widgetRefresher = mock<WidgetRefresher>()

    private val languageItem = LanguageItem("testLanguage", "testLanguageName", "testLanguageCode")
            .apply { id = 1 }

    private val date = Date().withZeroDayTime()

    @Before
    fun setUp() {
        repository = ViewPagerRepository(
                TestExecutor(),
                dailyLosungItemDao,
                languageItemDao,
                rawToDatabaseWriter,
                widgetRefresher)

        mockLanguageDatabaseResult(languageItem)
    }

    @Test
    fun `when 0 items available then repo tries to load from raw`() {
        setItemsAvailable(0)
        verify(rawToDatabaseWriter, times(1)).writeRawDataToDatabase(any())
    }

    @Test
    fun `when 99 items available then repo tries to load from raw`() {
        setItemsAvailable(99)
        verify(rawToDatabaseWriter, times(1)).writeRawDataToDatabase(any())
    }

    @Test
    fun `when 365 items available then no load from raw happening`() {
        setItemsAvailable(365)
        verify(rawToDatabaseWriter, never()).writeRawDataToDatabase(any())
    }

    @Test
    fun `when 366 items available then no download happening`() {
        setItemsAvailable(366)
        verify(rawToDatabaseWriter, never()).writeRawDataToDatabase(any())
    }

    private fun setItemsAvailable(amount: Int) {
        val list = (0 until amount).map { sampleDailyLosungItem(it + 1, languageItem.id) }
        mockLosungDatabaseResult(result = list)
        loadItemsFromRepo()
    }

    private fun loadItemsFromRepo() {
        val liveData = MutableLiveData<AsyncLoad<String>>()
        repository.loadItemsFor(
                language = languageItem.language,
                fromDate = date.firstDayOfYear(),
                toDate = date.lastDayOfYear(),
                result = liveData)
        val result = liveData.blockingObserve() ?: throw NullPointerException()
        assertEquals(AsyncLoadStatus.SUCCESS, result.loadStatus)
        assertEquals(languageItem.language, result.data)
    }

    private fun mockLosungDatabaseResult(result: List<DailyLosungDatabaseItem>) {
        whenever(dailyLosungItemDao.range(any(), any(), any()))
                .thenReturn(result)

        whenever(dailyLosungItemDao.all())
                .thenReturn(result)
    }

    private fun mockLanguageDatabaseResult(item: LanguageItem) {
        whenever(languageItemDao.all()).thenReturn(listOf(item))
        whenever(languageItemDao.find(any())).thenReturn(item)
    }

}