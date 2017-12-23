package de.reiss.android.losungen.language


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import de.reiss.android.losungen.App
import de.reiss.android.losungen.R
import de.reiss.android.losungen.architecture.AppFragment
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.language.list.LanguageListBuilder
import de.reiss.android.losungen.language.list.LanguageListItemAdapter
import de.reiss.android.losungen.model.Language
import de.reiss.android.losungen.util.extensions.onClick
import kotlinx.android.synthetic.main.language_fragment.*


class LanguageFragment : AppFragment<LanguageViewModel>(R.layout.language_fragment), LanguageClickListener {

    companion object {

        fun createInstance() = LanguageFragment()

    }

    private val appPreferences by lazy {
        App.component.appPreferences
    }

    private lateinit var languageListItemAdapter: LanguageListItemAdapter

    override fun initViews(layout: View) {
        languageListItemAdapter = LanguageListItemAdapter(languageClickListener = this)

        with(language_recycler_view) {
            layoutManager = LinearLayoutManager(context)
            adapter = languageListItemAdapter
        }

        language_no_languages.onClick {
            tryRefresh()
        }
    }

    override fun defineViewModelProvider(): ViewModelProvider =
            ViewModelProviders.of(this, LanguageViewModel.Factory(
                    App.component.languageRepository))

    override fun defineViewModel(): LanguageViewModel =
            loadViewModelProvider().get(LanguageViewModel::class.java)

    override fun initViewModelObservers() {
        viewModel.languagesLiveData.observe(this, Observer<AsyncLoad<List<Language>>> {
            onResourceChange()
        })
    }

    override fun onAppFragmentReady() {
        tryRefresh()
    }

    override fun onLanguageClicked(language: Language) {
        appPreferences.chosenLanguage = language.key
    }

    private fun tryRefresh() {
        if (viewModel.isLoadingLanguages().not()) {
            viewModel.refreshLanguages()
        }
    }

    private fun onResourceChange() {
        language_loading_languages.visibility = GONE
        language_no_languages.visibility = GONE
        language_recycler_view.visibility = GONE

        if (viewModel.isLoadingLanguages()) {
            language_loading_languages.visibility = VISIBLE
        } else {
            LanguageListBuilder.buildList(viewModel.languages()).let { listItems ->
                if (listItems.isEmpty()) {
                    language_no_languages.visibility = VISIBLE
                } else {
                    language_recycler_view.visibility = VISIBLE
                    languageListItemAdapter.updateContent(listItems)
                }
            }
        }
    }

}