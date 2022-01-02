package de.reiss.android.losungen.language


import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import de.reiss.android.losungen.App
import de.reiss.android.losungen.R
import de.reiss.android.losungen.architecture.AppFragment
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.databinding.LanguageFragmentBinding
import de.reiss.android.losungen.language.list.LanguageListBuilder
import de.reiss.android.losungen.language.list.LanguageListItemAdapter
import de.reiss.android.losungen.model.Language
import de.reiss.android.losungen.util.extensions.onClick


class LanguageFragment :
    AppFragment<LanguageFragmentBinding, LanguageViewModel>(R.layout.language_fragment),
    LanguageClickListener {

    companion object {

        fun createInstance() = LanguageFragment()

    }

    private val appPreferences by lazy {
        App.component.appPreferences
    }

    private lateinit var languageListItemAdapter: LanguageListItemAdapter

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        LanguageFragmentBinding.inflate(inflater, container, false)

    override fun initViews() {
        languageListItemAdapter = LanguageListItemAdapter(languageClickListener = this)

        with(binding.languageRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = languageListItemAdapter
        }

        binding.languageNoLanguages.onClick {
            tryRefresh()
        }
    }

    override fun defineViewModelProvider(): ViewModelProvider =
        ViewModelProviders.of(
            this, LanguageViewModel.Factory(
                App.component.languageRepository
            )
        )

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
        binding.languageLoadingLanguages.visibility = GONE
        binding.languageNoLanguages.visibility = GONE
        binding.languageRecyclerView.visibility = GONE

        if (viewModel.isLoadingLanguages()) {
            binding.languageLoadingLanguages.visibility = VISIBLE
        } else {
            LanguageListBuilder.buildList(viewModel.languages()).let { listItems ->
                if (listItems.isEmpty()) {
                    binding.languageNoLanguages.visibility = VISIBLE
                } else {
                    binding.languageRecyclerView.visibility = VISIBLE
                    languageListItemAdapter.updateContent(listItems)
                }
            }
        }
    }

}