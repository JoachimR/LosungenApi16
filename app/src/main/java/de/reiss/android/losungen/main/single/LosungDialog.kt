package de.reiss.android.losungen.main.single

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModel
import de.reiss.android.losungen.R
import de.reiss.android.losungen.architecture.AppDialogFragment
import de.reiss.android.losungen.util.view.FadingProgressBar

abstract class LosungDialog<T : ViewModel> : AppDialogFragment<T>() {

    protected lateinit var contentEmpty: View
    protected lateinit var content: View
    protected lateinit var text: TextView
    protected lateinit var source: TextView
    protected lateinit var loading: FadingProgressBar

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog =
            requireActivity().let { activity ->
                AlertDialog.Builder(activity)
                        .setTitle(title())
                        .setNeutralButton(R.string.dialog_share, { _, _ ->
                            share()
                            dismiss()
                        })
                        .setPositiveButton(R.string.dialog_close, { _, _ ->
                            dismiss()
                        })
                        .setView(initLayout(activity))
                        .create()
            }

    @SuppressLint("InflateParams")
    override fun inflateLayout(activity: Activity): View =
            activity.layoutInflater.inflate(R.layout.losung_dialog, null)

    override fun initViews(layout: View) {
        contentEmpty = layout.findViewById<View>(R.id.losung_dialog_content_empty)
        content = layout.findViewById<View>(R.id.losung_dialog_content)
        text = layout.findViewById(R.id.losung_dialog_text)
        source = layout.findViewById(R.id.losung_dialog_source)
        loading = layout.findViewById(R.id.losung_dialog_loading)
    }

    abstract fun startLoadLosung()

    override fun onStart() {
        super.onStart()
        startLoadLosung()
    }

    protected fun shareButton() =
            (dialog as AlertDialog).getButton(AlertDialog.BUTTON_NEUTRAL)

    abstract fun share()

}