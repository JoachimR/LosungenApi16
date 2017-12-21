package de.reiss.android.losungen.main.content

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.SeekBar
import de.reiss.android.losungen.App
import de.reiss.android.losungen.R
import de.reiss.android.losungen.preferences.AppPreferences

class FontSizePreferenceDialog : DialogFragment() {

    companion object {

        fun createInstance() = FontSizePreferenceDialog()

    }

    private val appPreferences: AppPreferences by lazy {
        App.component.appPreferences
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            activity.let { activity ->
                if (activity == null) {
                    throw NullPointerException()
                }
                AlertDialog.Builder(activity)
                        .setTitle(getString(R.string.fontsize_dialog_title))
                        .setCancelable(true)
                        .setPositiveButton(activity.getString(R.string.fontsize_dialog_ok)) { _, _ ->
                            dismiss()
                        }
                        .setView(initDialogUi())
                        .create()
            }

    private fun initDialogUi(): View {
        activity.let { activity ->
            if (activity == null) {
                throw NullPointerException()
            }

            val view = activity.layoutInflater.inflate(R.layout.fontsize_dialog, null)

            view.findViewById<SeekBar>(R.id.fontsize_dialog_seekbar).apply {

                progress = appPreferences.fontSize()

                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

                    override fun onProgressChanged(seekBar: SeekBar,
                                                   progress: Int,
                                                   fromUser: Boolean) {
                        if (fromUser) {
                            appPreferences.changeFontSize(newFontSize = progress)
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar) {
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar) {
                    }

                })
            }

            return view
        }
    }

}
