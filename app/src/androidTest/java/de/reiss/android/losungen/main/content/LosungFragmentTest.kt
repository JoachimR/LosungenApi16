package de.reiss.android.losungen.main.content

import android.arch.lifecycle.MutableLiveData
import android.support.test.runner.AndroidJUnit4
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import de.reiss.android.losungen.DaysPositionUtil
import de.reiss.android.losungen.R
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.model.DailyLosung
import de.reiss.android.losungen.model.Note
import de.reiss.android.losungen.testutil.*
import de.reiss.android.losungen.util.extensions.withZeroDayTime
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class LosungFragmentTest : FragmentTest<LosungFragment>() {

    private val losungLiveData = MutableLiveData<AsyncLoad<DailyLosung>>()
    private val noteLiveData = MutableLiveData<AsyncLoad<Note>>()

    private val mockedViewModel = mock<LosungViewModel> {
        on { losungLiveData() } doReturn losungLiveData
        on { noteLiveData() } doReturn noteLiveData
    }

    override fun createFragment(): LosungFragment =
            LosungFragment.createInstance(
                    DaysPositionUtil.positionFor(timeForTest()))
                    .apply {
                        viewModelProvider = mock {
                            on { get(any<Class<LosungViewModel>>()) } doReturn mockedViewModel
                        }
                    }

    @Before
    fun setUp() {
        launchFragment()
    }

    @Test
    fun whenLoadingThenShowLoading() {
        losungLiveData.postValue(AsyncLoad.loading())

        assertDisplayed(R.id.losung_loading)
        assertNotDisplayed(R.id.losung_empty_root, R.id.losung_content_root)
    }

    @Test
    fun whenEmptyThenShowEmpty() {
        losungLiveData.postValue(AsyncLoad.success(null))

        assertDisplayed(R.id.losung_empty_root)
        assertNotDisplayed(R.id.losung_loading, R.id.losung_content_root)
    }

    @Test
    fun whenContentThenShowContent() {
        val dailyLosung = sampleDailyLosung(number = 0, language = "testLanguage")
        losungLiveData.postValue(AsyncLoad.success(dailyLosung))

        assertDisplayed(R.id.losung_content_root)
        assertNotDisplayed(R.id.losung_loading, R.id.losung_empty_root)
        checkIsTextSet {
            R.id.losung_text1 to dailyLosung.content.text1
        }
        checkIsTextSet {
            R.id.losung_source1 to dailyLosung.content.source1
        }
        checkIsTextSet {
            R.id.losung_text2 to dailyLosung.content.text2
        }
        checkIsTextSet {
            R.id.losung_source2 to dailyLosung.content.source2
        }
    }

    private fun timeForTest() = Calendar.getInstance().apply {
        time = time.withZeroDayTime()
    }

}