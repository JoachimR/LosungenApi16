package de.reiss.android.losungen.main

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.MenuItem
import android.view.WindowManager
import de.reiss.android.losungen.App
import de.reiss.android.losungen.DaysPositionUtil
import de.reiss.android.losungen.R
import de.reiss.android.losungen.about.AboutActivity
import de.reiss.android.losungen.architecture.AppActivity
import de.reiss.android.losungen.language.LanguageActivity
import de.reiss.android.losungen.main.viewpager.ViewPagerFragment
import de.reiss.android.losungen.note.list.NoteListActivity
import de.reiss.android.losungen.preferences.AppPreferencesActivity
import de.reiss.android.losungen.util.extensions.findFragmentIn
import de.reiss.android.losungen.util.extensions.replaceFragmentIn
import kotlinx.android.synthetic.main.main_activity_no_toolbar.*
import java.util.*


class MainActivityNoToolbar : AppActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, MainActivityNoToolbar::class.java)
        }

    }

    private val appPreferences by lazy {
        App.component.appPreferences
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_no_toolbar)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }

        if (redirectIfChosenLanguageMissing()) {
            return
        }

        main_no_toolbar_nav.setNavigationItemSelectedListener(this)
        refreshFragment()
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.main_no_toolbar_drawer)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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
        main_no_toolbar_drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun refreshFragment() {
        if (findFragmentIn(R.id.main_no_toolbar_fragment_container) == null) {
            replaceFragmentIn(
                    container = R.id.main_no_toolbar_fragment_container,
                    fragment = ViewPagerFragment.createInstance(
                            DaysPositionUtil.positionFor(Calendar.getInstance())
                    )
            )
        }
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
