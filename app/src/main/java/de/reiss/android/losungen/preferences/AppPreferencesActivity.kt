package de.reiss.android.losungen.preferences

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import de.reiss.android.losungen.App
import de.reiss.android.losungen.R
import de.reiss.android.losungen.SplashScreenActivity
import de.reiss.android.losungen.architecture.AppActivity
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.model.Language
import de.reiss.android.losungen.util.extensions.replaceFragmentIn
import kotlinx.android.synthetic.main.preference_activity.*

class AppPreferencesActivity : AppActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {

        fun createIntent(context: Context): Intent =
                Intent(context, AppPreferencesActivity::class.java)

    }

    lateinit var viewModel: AppPreferencesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.preference_activity)
        setSupportActionBar(preferences_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProviders.of(this, AppPreferencesViewModel.Factory(
                App.component.appPreferencesRepository))
                .get(AppPreferencesViewModel::class.java)

        viewModel.languagesLiveData.observe(this, Observer<AsyncLoad<List<Language>>> {
            updateUi()
        })

        App.component.appPreferences.registerListener(this)
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadLanguages()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == getString(R.string.pref_theme_key)
                || key == getString(R.string.pref_show_toolbar_key)
                || key == getString(R.string.pref_show_cards_key)) {
            restartApp()
        }
    }

    private fun updateUi() {
        if (viewModel.isLoadingLanguages()) {
            preferences_loading.loading = true
            preferences_fragment_container.visibility = GONE
        } else {
            preferences_loading.loading = false
            preferences_fragment_container.visibility = VISIBLE

            if (supportFragmentManager.findFragmentById(R.id.preferences_fragment_container) == null) {
                replaceFragmentIn(R.id.preferences_fragment_container,
                        AppPreferencesFragment.newInstance(viewModel.languages()))

            }
        }
    }

    private fun restartApp() {
        startActivity(SplashScreenActivity.createIntent(this)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
        supportFinishAfterTransition()
    }

}
