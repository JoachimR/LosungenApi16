package de.reiss.android.losungen.note.list

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NoteExportViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: NoteListViewModel
    private val repository: NoteListRepository = mock()

    @Before
    fun setUp() {
        viewModel = NoteListViewModel(repository)
    }

    @Test
    fun loadLosung() {
        viewModel.loadNotes()
        verify(repository).getAllNotes(any())
    }

    @Test
    fun applyFilter() {
        val query = "myFilter"
        viewModel.applyNewFilter(query)
        verify(repository).applyNewFilter(eq(query), any())
    }

}