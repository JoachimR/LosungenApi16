package de.reiss.android.losungen.note.list

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import de.reiss.android.losungen.R
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.model.Note
import de.reiss.android.losungen.testutil.*
import org.junit.Before
import org.junit.Test

class NoteListFragmentTest : FragmentTest<NoteListFragment>() {

    private val notesLiveData = MutableLiveData<AsyncLoad<FilteredNotes>>()

    private val mockedViewModel = mock<NoteListViewModel> {
        on { notesLiveData() } doReturn notesLiveData
    }

    override fun createFragment(): NoteListFragment =
            NoteListFragment.createInstance()
                    .apply {
                        viewModelProvider = mock {
                            on { get(any<Class<NoteListViewModel>>()) } doReturn mockedViewModel
                        }
                    }

    @Before
    fun setUp() {
        launchFragment()
    }

    @Test
    fun whenLoadingThenShowLoading() {
        notesLiveData.postValue(AsyncLoad.loading())
        Thread.sleep(100)

        assertDisplayed(R.id.note_list_loading)
    }

    @Test
    fun whenLoadedNoNotesThenShowEmptyState() {
        notesLiveData.postValue(AsyncLoad.success(FilteredNotes()))
        Thread.sleep(100)

        assertNotDisplayed(R.id.note_list_loading)

        assertDisplayed(R.id.note_list_no_notes)
        assertNotDisplayed(R.id.note_list_recycler_view)
    }

    @Test
    fun whenLoaded1NoteThenShowListWith1Item() {
        val notes = listOf(sampleNote(0))
        notesLiveData.postValue(AsyncLoad.success(FilteredNotes(notes, notes, "")))
        Thread.sleep(100)

        assertNotDisplayed(R.id.note_list_no_notes)

        assertDisplayed(R.id.note_list_recycler_view)
        assertRecyclerViewItemsCount(R.id.note_list_recycler_view, 1)
        assertNoteIsDisplayedAt(note = notes.first(), index = 0)
    }

    @Test
    fun whenLoaded99NotesThenShowListWith99Items() {
        val notes = (1..99).map { sampleNote(it) }
        notesLiveData.postValue(AsyncLoad.success(FilteredNotes(notes, notes, "")))
        Thread.sleep(100)

        assertNotDisplayed(R.id.note_list_no_notes)
        assertNotDisplayed(R.id.note_list_loading)

        assertDisplayed(R.id.note_list_recycler_view)
        assertRecyclerViewItemsCount(R.id.note_list_recycler_view, 99)
        for ((index, note) in notes.withIndex()) {
            assertNoteIsDisplayedAt(note = note, index = index)
        }
    }

    private fun assertNoteIsDisplayedAt(note: Note, index: Int) {
        onRecyclerView(
                recyclerViewResId = R.id.note_list_recycler_view,
                itemPosition = index,
                viewInItem = R.id.note_list_item_text)
                .check(matches(withText(note.noteText)))
    }

}