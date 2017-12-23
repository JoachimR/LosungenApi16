package de.reiss.android.losungen.language

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import de.reiss.android.losungen.App
import de.reiss.android.losungen.R
import de.reiss.android.losungen.main.MainActivity
import de.reiss.android.losungen.main.MainActivityNoToolbar
import de.reiss.android.losungen.util.extensions.findFragmentIn
import de.reiss.android.losungen.util.extensions.replaceFragmentIn
import kotlinx.android.synthetic.main.language_activity.*

class LanguageActivity : AppCompatActivity() {

    companion object {

        fun createIntent(context: Context): Intent = Intent(context, LanguageActivity::class.java)

    }

    private val appPreferences by lazy {
        App.component.appPreferences
    }

    private val prefChangedListener: SharedPreferences.OnSharedPreferenceChangeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
                redirectIfLanguageChosen()
            }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.language_activity)
        setSupportActionBar(language_toolbar)

        if (redirectIfLanguageChosen()) {
            return
        }

        appPreferences.registerListener(prefChangedListener)

        initFragment()
    }

    private fun initFragment() {
        if (findFragmentIn(R.id.language_fragment_container) == null) {
            replaceFragmentIn(
                    container = R.id.language_fragment_container,
                    fragment = LanguageFragment.createInstance()
            )
        }
    }

    private fun redirectIfLanguageChosen(): Boolean {
        if (appPreferences.chosenLanguage != null) {
            startActivity(
                    if (appPreferences.showToolbar()) MainActivity.createIntent(this)
                    else MainActivityNoToolbar.createIntent(this)
            )
            supportFinishAfterTransition()
            return true
        }
        return false
    }

}