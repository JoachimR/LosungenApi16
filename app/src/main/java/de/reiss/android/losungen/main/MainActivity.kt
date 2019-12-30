package de.reiss.android.losungen.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import com.google.android.material.navigation.NavigationView
import de.reiss.android.losungen.R


class MainActivity : AbstractMainActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }

    }

    override fun setContent() {
        setContentView(R.layout.main_activity)
        setSupportActionBar(toolbar())
    }

    override fun initNavigation() {
        drawer = findViewById(R.id.main_drawer)
        navigationView = findViewById(R.id.main_nav)
        val toggle = ActionBarDrawerToggle(this,
                drawer,
                toolbar(),
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun fragmentResId(): Int = R.id.main_fragment_container

    private fun toolbar() = findViewById<Toolbar>(R.id.main_toolbar)

}
