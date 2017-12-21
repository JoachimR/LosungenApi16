package de.reiss.android.losungen.testutil

import android.annotation.SuppressLint
import android.app.Activity
import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.v4.app.Fragment
import de.reiss.android.losungen.R
import de.reiss.android.losungen.UnderTestAppActivity
import org.junit.Rule
import java.lang.RuntimeException


abstract class FragmentTest<T : Fragment> {

    companion object {

        val FRAGMENT_TAG = "fragment_under_test"

    }

    @get:Rule
    var activityRule = ActivityTestRule(UnderTestAppActivity::class.java)

    val activity: Activity
        get() = activityRule.activity

    var fragment: T? = null

    protected abstract fun createFragment(): T

    @SuppressLint("CommitTransaction")
    protected fun launchFragment() {
        val fragmentManager = activityRule.activity.supportFragmentManager

        runOnUiThreadAndIdleSync(activityRule,
                Runnable {
                    fragment = createFragment()
                    fragmentManager
                            .beginTransaction()
                            .replace(R.id.under_test_content_view, fragment, FRAGMENT_TAG)
                            .commitNow()
                })
    }

    private fun runOnUiThreadAndIdleSync(rule: ActivityTestRule<*>, runnable: Runnable) {
        try {
            rule.runOnUiThread(runnable)
            InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        } catch (throwable: Throwable) {
            throw RuntimeException(throwable)
        }
    }

}