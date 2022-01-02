package de.reiss.android.losungen.main.single.yearly

import android.content.Context
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import de.reiss.android.losungen.App
import de.reiss.android.losungen.R
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.main.single.LosungDialog
import de.reiss.android.losungen.model.YearlyLosung
import de.reiss.android.losungen.util.contentAsString
import de.reiss.android.losungen.util.shareIntent

class YearlyLosungDialog : LosungDialog<YearlyLosungViewModel>() {

    companion object {

        fun createInstance() = YearlyLosungDialog()

    }

    override fun defineViewModelProvider(): ViewModelProvider =
        ViewModelProviders.of(
            this, YearlyLosungViewModel.Factory(
                App.component.yearlyLosungRepository
            )
        )

    override fun defineViewModel(): YearlyLosungViewModel =
        loadViewModelProvider().get(YearlyLosungViewModel::class.java)

    override fun title() = getString(R.string.yearly_dialog_title)

    override fun initViewModelObservers() {
        viewModel.losungLiveData().observe(this, Observer<AsyncLoad<YearlyLosung?>> {
            updateUi()
        })
    }

    override fun startLoadLosung() {
        viewModel.loadCurrent()
    }

    override fun share() {
        val context = context ?: return
        viewModel.losung()?.let {
            startActivity(
                shareIntent(
                    text = contentAsString(dateText(context, it), it.bibleText),
                    chooserTitle = context.getString(R.string.share_dialog_chooser_title)
                )
            )
        }
    }

    private fun updateUi() {
        val context = context ?: return
        val losung = viewModel.losung()
        when {
            viewModel.isLoading() -> {
                loading.loading = true
                contentEmpty.visibility = GONE
                content.visibility = GONE
                shareButton()?.isEnabled = false
            }

            (viewModel.isError() || losung == null) -> {
                loading.loading = false
                contentEmpty.visibility = VISIBLE
                content.visibility = GONE
                shareButton()?.isEnabled = false
            }

            viewModel.isSuccess() -> {
                loading.loading = false
                contentEmpty.visibility = GONE
                content.visibility = VISIBLE

                text.text = losung.bibleText.text
                source.text = losung.bibleText.source

                dialog!!.setTitle(dateText(context, losung))
                shareButton()?.isEnabled = true
            }
        }
    }

    private fun dateText(context: Context, losung: YearlyLosung) =
        context.getString(
            R.string.yearly_dialog_title_with_date,
            losung.year
        )

}