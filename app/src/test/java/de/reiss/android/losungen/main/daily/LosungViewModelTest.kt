package de.reiss.android.losungen.main.daily

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@RunWith(JUnit4::class)
class LosungViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: LosungViewModel
    private val repository: LosungRepository = mock()

    @Before
    fun setUp() {
        viewModel = LosungViewModel(repository)
    }

    @Test
    fun loadLosung() {
        val date = Date()

        viewModel.loadLosung(date)

        argumentCaptor<Date>().apply {
            verify(repository).getLosungFor(capture(), any())
            assertEquals(date, allValues[0])
        }
    }

    @Test
    fun loadNote() {
        val date = Date()

        viewModel.loadNote(date)

        argumentCaptor<Date>().apply {
            verify(repository).getNoteFor(capture(), any())
            assertEquals(date, allValues[0])
        }
    }

}