package de.reiss.android.losungen.language

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import de.reiss.android.losungen.App
import de.reiss.android.losungen.R
import de.reiss.android.losungen.databinding.AboutActivityBinding
import de.reiss.android.losungen.databinding.LanguageActivityBinding
import de.reiss.android.losungen.main.MainActivity
import de.reiss.android.losungen.main.MainActivityNoToolbar
import de.reiss.android.losungen.util.extensions.findFragmentIn
import de.reiss.android.losungen.util.extensions.replaceFragmentIn

class LanguageActivity : AppCompatActivity() {

    companion object {

        fun createIntent(context: Context): Intent = Intent(context, LanguageActivity::class.java)

    }

    private lateinit var binding: LanguageActivityBinding


    private val appPreferences by lazy {
        App.component.appPreferences
    }

    private val prefChangedListener: SharedPreferences.OnSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            redirectIfLanguageChosen()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LanguageActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.languageToolbar)

        if (redirectIfLanguageChosen()) {
            return
        }

        initFragment()
    }

    override fun onStart() {
        super.onStart()
        appPreferences.registerListener(prefChangedListener)
    }

    override fun onPause() {
        appPreferences.unregisterListener(prefChangedListener)
        super.onPause()
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