package de.reiss.android.losungen.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.MenuItem
import de.reiss.android.losungen.App
import de.reiss.android.losungen.DaysPositionUtil
import de.reiss.android.losungen.R
import de.reiss.android.losungen.about.AboutActivity
import de.reiss.android.losungen.architecture.AppActivity
import de.reiss.android.losungen.language.LanguageActivity
import de.reiss.android.losungen.main.daily.viewpager.ViewPagerFragment
import de.reiss.android.losungen.main.single.monthly.MonthlyLosungDialog
import de.reiss.android.losungen.main.single.weekly.WeeklyLosungDialog
import de.reiss.android.losungen.main.single.yearly.YearlyLosungDialog
import de.reiss.android.losungen.note.list.NoteListActivity
import de.reiss.android.losungen.preferences.AppPreferencesActivity
import de.reiss.android.losungen.util.extensions.displayDialog
import de.reiss.android.losungen.util.extensions.findFragmentIn
import de.reiss.android.losungen.util.extensions.replaceFragmentIn
import java.util.*


abstract class AbstractMainActivity : AppActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, AbstractMainActivity::class.java)
        }

    }

    private val appPreferences by lazy {
        App.component.appPreferences
    }

    protected lateinit var drawer: DrawerLayout
    protected lateinit var navigationView: NavigationView

    protected abstract fun setContent()

    protected abstract fun initNavigation()

    protected abstract fun fragmentResId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent()
        initNavigation()

        if (redirectIfChosenLanguageMissing()) {
            return
        }

        initNavigation()
        refreshFragment()
    }

    private fun refreshFragment() {
        if (findFragmentIn(fragmentResId()) == null) {
            replaceFragmentIn(
                    container = fragmentResId(),
                    fragment = ViewPagerFragment.createInstance(
                            DaysPositionUtil.positionFor(Calendar.getInstance())
                    )
            )
        }
    }


    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_losung_week -> {
                displayDialog(WeeklyLosungDialog.createInstance())
            }
            R.id.nav_losung_month -> {
                displayDialog(MonthlyLosungDialog.createInstance())
            }
            R.id.nav_losung_year -> {
                displayDialog(YearlyLosungDialog.createInstance())
            }
            R.id.nav_all_notes -> {
                startActivity(NoteListActivity.createIntent(this))
            }
            R.id.nav_settings -> {
                goToSettings()
            }
            R.id.nav_info -> {
                startActivity(AboutActivity.createIntent(this))
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun redirectIfChosenLanguageMissing(): Boolean {
        val chosenLanguage = appPreferences.chosenLanguage
        if (chosenLanguage == null) {
            startActivity(LanguageActivity.createIntent(this))
            supportFinishAfterTransition()
            return true
        }
        return false
    }

    private fun goToSettings() {
        startActivity(AppPreferencesActivity.createIntent(this))
    }

}
