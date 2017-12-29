package de.reiss.android.losungen.main.daily.viewpager

import android.arch.lifecycle.MutableLiveData
import android.support.test.runner.AndroidJUnit4
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import de.reiss.android.losungen.R
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.testutil.FragmentTest
import de.reiss.android.losungen.testutil.assertDisplayed
import de.reiss.android.losungen.testutil.assertNotDisplayed
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ViewPagerFragmentTest : FragmentTest<ViewPagerFragment>() {

    private val chosenLanguage = "losungen_xzy"

    private val loadYearLiveData = MutableLiveData<AsyncLoad<String>>()

    private val mockedViewModel = mock<ViewPagerViewModel> {
        on { loadYearLiveData() } doReturn loadYearLiveData
    }

    override fun createFragment(): ViewPagerFragment =
            ViewPagerFragment.createInstance()
                    .apply {
                        viewModelProvider = mock {
                            on { get(any<Class<ViewPagerViewModel>>()) } doReturn mockedViewModel
                        }
                        adapterCreator = mock {
                            on { create(any()) } doReturn mock<ViewPagerAdapter>()
                        }
                    }

    @Before
    fun setUp() {
        loadYearLiveData.postValue(AsyncLoad.success(chosenLanguage))
        launchFragment()
    }

    @Test
    fun whenLoadingThenShowLoading() {
        loadYearLiveData.postValue(AsyncLoad.loading(chosenLanguage))

        assertDisplayed(R.id.view_pager_loading)
        assertNotDisplayed(R.id.view_pager)
    }

    @Test
    fun whenDoneLoadingThenShowViewPager() {
        loadYearLiveData.postValue(AsyncLoad.success(chosenLanguage))

        assertDisplayed(R.id.view_pager)
        assertNotDisplayed(R.id.view_pager_loading)
    }

}
