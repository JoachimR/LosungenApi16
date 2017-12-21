package de.reiss.android.losungen.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
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
import kotlinx.android.synthetic.main.main_activity.*
import java.util.*


class MainActivity : AppActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {

        fun createIntent(context: Context): Intent = Intent(context, MainActivity::class.java)

    }

    private val appPreferences by lazy {
        App.component.appPreferences
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(main_toolbar)

        if (redirectIfChosenLanguageMissing()) {
            return
        }

        initNav()
        refreshFragment()
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.main_drawer)
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
        main_drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun refreshFragment() {
        if (findFragmentIn(R.id.main_fragment_container) == null) {
            replaceFragmentIn(
                    container = R.id.main_fragment_container,
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

    private fun initNav() {
        val toggle = ActionBarDrawerToggle(this,
                main_drawer,
                main_toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        main_drawer.addDrawerListener(toggle)
        toggle.syncState()

        main_nav.setNavigationItemSelectedListener(this)
    }

    private fun goToSettings() {
        startActivity(AppPreferencesActivity.createIntent(this))
    }

}
