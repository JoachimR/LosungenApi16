package de.reiss.android.losungen.main

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.WindowManager
import com.google.android.material.navigation.NavigationView
import de.reiss.android.losungen.R


class MainActivityNoToolbar : AbstractMainActivity(),
    NavigationView.OnNavigationItemSelectedListener {

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, MainActivityNoToolbar::class.java)
        }

    }

    override fun setContent() {
        setContentView(R.layout.main_activity_no_toolbar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
    }

    override fun initNavigation() {
        drawer = findViewById(R.id.main_no_toolbar_drawer)
        navigationView = findViewById(R.id.main_no_toolbar_nav)
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun fragmentResId(): Int = R.id.main_no_toolbar_fragment_container

}
