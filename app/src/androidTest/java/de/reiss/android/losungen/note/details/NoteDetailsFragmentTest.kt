package de.reiss.android.losungen.note.details

import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import de.reiss.android.losungen.R
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.model.Note
import de.reiss.android.losungen.testutil.*
import de.reiss.android.losungen.util.contentAsString
import org.junit.Before
import org.junit.Test

class NoteDetailsFragmentTest : FragmentTest<NoteDetailsFragment>() {

    private val noteLiveData = MutableLiveData<AsyncLoad<Note>>()
    private val deleteLiveData = MutableLiveData<AsyncLoad<Void>>()

    private val note = sampleNote(0)

    private val mockedViewModel = mock<NoteDetailsViewModel> {
        on { noteLiveData() } doReturn noteLiveData
        on { deleteLiveData() } doReturn deleteLiveData
    }

    override fun createFragment(): NoteDetailsFragment =
            NoteDetailsFragment.createInstance(note)
                    .apply {
                        viewModelProvider = mock {
                            on { get(any<Class<NoteDetailsViewModel>>()) } doReturn mockedViewModel
                        }
                    }

    @Before
    fun setUp() {
        launchFragment()
    }

    @Test
    fun whenLoadingThenShowLoading() {
        noteLiveData.postValue(AsyncLoad.loading())
        Thread.sleep(100)

        assertDisplayed(R.id.note_details_loading)
    }

    @Test
    fun whenDeletingThenShowLoading() {
        deleteLiveData.postValue(AsyncLoad.loading())
        Thread.sleep(100)

        assertDisplayed(R.id.note_details_loading)
    }

    @Test
    fun whenDeletedThenFinishing() {
        deleteLiveData.postValue(AsyncLoad.success())
        Thread.sleep(100)

        assertActivityIsFinished(activityRule)
    }

    @Test
    fun whenLoadSuccessThenShowContent() {
        noteLiveData.postValue(AsyncLoad.success(note))
        Thread.sleep(100)

        checkTextsAreDisplayed(
                contentAsString(activity, note.date.time, note.bibleTextPair, ""),
                note.noteText)

        assertNotDisplayed(R.id.note_details_loading)
    }

}