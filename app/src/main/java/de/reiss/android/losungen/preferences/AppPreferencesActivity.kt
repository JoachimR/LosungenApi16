package de.reiss.android.losungen.preferences

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import de.reiss.android.losungen.App
import de.reiss.android.losungen.R
import de.reiss.android.losungen.architecture.AppActivity
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.databinding.PreferenceActivityBinding
import de.reiss.android.losungen.model.Language
import de.reiss.android.losungen.util.extensions.replaceFragmentIn

class AppPreferencesActivity : AppActivity() {

    companion object {

        fun createIntent(context: Context): Intent =
            Intent(context, AppPreferencesActivity::class.java)

    }

    lateinit var viewModel: AppPreferencesViewModel


    private lateinit var binding: PreferenceActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = PreferenceActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.preferencesToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProviders.of(
            this, AppPreferencesViewModel.Factory(
                App.component.appPreferencesRepository
            )
        )
            .get(AppPreferencesViewModel::class.java)

        viewModel.languagesLiveData.observe(this, Observer<AsyncLoad<List<Language>>> {
            updateUi()
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadLanguages()
    }

    private fun updateUi() {
        if (viewModel.isLoadingLanguages()) {
            binding.preferencesLoading.loading = true
            binding.preferencesFragmentContainer.visibility = GONE
        } else {
            binding.preferencesLoading.loading = false
            binding.preferencesFragmentContainer.visibility = VISIBLE

            if (supportFragmentManager.findFragmentById(R.id.preferences_fragment_container) == null) {
                replaceFragmentIn(
                    R.id.preferences_fragment_container,
                    AppPreferencesFragment.createInstance(viewModel.languages())
                )

            }
        }
    }

}
