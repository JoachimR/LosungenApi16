package de.reiss.android.losungen.note.edit

import android.arch.lifecycle.MutableLiveData
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import de.reiss.android.losungen.R
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.model.Note
import de.reiss.android.losungen.testutil.*
import org.junit.Before
import org.junit.Test
import java.util.*

class EditNoteFragmentTest : FragmentTest<EditNoteFragment>() {

    private val loadNoteLiveData = MutableLiveData<AsyncLoad<Note?>>()
    private val storeNoteLiveData = MutableLiveData<AsyncLoad<Void>>()

    private val mockedViewModel = mock<EditNoteViewModel> {
        on { loadNoteLiveData() } doReturn loadNoteLiveData
        on { storeNoteLiveData() } doReturn storeNoteLiveData
    }

    override fun createFragment(): EditNoteFragment =
            EditNoteFragment.createInstance(Date().time, bibleTextPair(0))
                    .apply {
                        viewModelProvider = mock {
                            on { get(any<Class<EditNoteViewModel>>()) } doReturn mockedViewModel
                        }
                    }

    @Before
    fun setUp() {
        launchFragment()
    }

    @Test
    fun whenLoadingThenShowLoading() {
        loadNoteLiveData.postValue(AsyncLoad.loading())
        Thread.sleep(500)

        assertDisplayed(R.id.edit_note_loading)
        assertNotDisplayed(R.id.edit_note_input, R.id.edit_note_load_error)
    }

    @Test
    fun whenStoringThenShowLoading() {
        storeNoteLiveData.postValue(AsyncLoad.loading())
        Thread.sleep(500)

        assertDisplayed(R.id.edit_note_loading)
        assertNotDisplayed(R.id.edit_note_input, R.id.edit_note_load_error)
    }

    @Test
    fun whenLoadSuccessThenShowInput() {
        loadNoteLiveData.postValue(AsyncLoad.success(sampleNote(0)))
        Thread.sleep(500)

        assertDisplayed(R.id.edit_note_input)
        assertNotDisplayed(R.id.edit_note_loading, R.id.edit_note_load_error)
    }

    @Test
    fun whenLoadSuccessButNothingFoundThenShowEmptyNote() {
        loadNoteLiveData.postValue(AsyncLoad.success(null))
        Thread.sleep(500)

        assertDisplayed(R.id.edit_note_input)
        checkIsTextSet { R.id.edit_note_input to "" }
        assertNotDisplayed(R.id.edit_note_loading, R.id.edit_note_load_error)
    }

    @Test
    fun whenLoadErrorThenHideInputAndShowLoadError() {
        loadNoteLiveData.postValue(AsyncLoad.error())
        Thread.sleep(500)

        assertDisplayed(R.id.edit_note_load_error)
        assertNotDisplayed(R.id.edit_note_loading, R.id.edit_note_input)
    }

    @Test
    fun whenStoreErrorThenShowInputAndShowStoreErrorSnackbar() {
        storeNoteLiveData.postValue(AsyncLoad.error())
        Thread.sleep(500)

        assertDisplayed(R.id.edit_note_input)
        assertNotDisplayed(R.id.edit_note_loading, R.id.edit_note_load_error)
        assertTextInSnackbar(activity.getString(R.string.edit_note_store_error))
    }

}