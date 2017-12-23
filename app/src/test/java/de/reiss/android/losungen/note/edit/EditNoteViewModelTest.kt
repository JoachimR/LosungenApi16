package de.reiss.android.losungen.note.edit

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import de.reiss.android.losungen.testutil.bibleTextPair
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class EditNoteViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: EditNoteViewModel
    private val repository: EditNoteRepository = mock()

    @Before
    fun setUp() {
        viewModel = EditNoteViewModel(repository)
    }

    @Test
    fun loadNote() {
        val date = Date()
        viewModel.loadNote(date)
        verify(repository).loadNote(eq(date), any())
    }

    @Test
    fun storeNote() {
        val date = Date()
        val text = "some text to store"
        val content = bibleTextPair(0)
        viewModel.storeNote(date, text, content)
        verify(repository).updateNote(eq(date), eq(text), eq(content), any())
    }

}