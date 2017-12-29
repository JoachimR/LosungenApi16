package de.reiss.android.losungen.main.daily.viewpager

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import de.reiss.android.losungen.util.extensions.firstDayOfYear
import de.reiss.android.losungen.util.extensions.lastDayOfYear
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@Suppress("IllegalIdentifier")
class ViewPagerViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ViewPagerViewModel
    private val repository: ViewPagerRepository = mock()

    private val language = "testLanguage"

    private val date = Date()


    @Before
    fun setUp() {
        viewModel = ViewPagerViewModel(initialLanguage = language, repository = repository)
    }

    @Test
    fun `when prepare is called then ask repo for whole year`() {
        viewModel.prepareContentFor(language, date)

        val languageCaptor = argumentCaptor<String>()
        val dateCaptor = argumentCaptor<Date>()

        verify(repository).loadItemsFor(
                language = languageCaptor.capture(),
                fromDate = dateCaptor.capture(),
                toDate = dateCaptor.capture(),
                result = any())

        assertEquals(language, languageCaptor.firstValue)
        assertEquals(date.firstDayOfYear(), dateCaptor.firstValue)
        assertEquals(date.lastDayOfYear(), dateCaptor.secondValue)
    }

}