package de.reiss.android.losungen.main.daily

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.EditText
import de.reiss.android.losungen.R
import de.reiss.android.losungen.model.BibleTextPair
import de.reiss.android.losungen.util.contentAsString
import de.reiss.android.losungen.util.shareIntent

class ShareDialog : DialogFragment() {

    companion object {

        private val KEY_INITIAL_CONTENT = "KEY_INITIAL_CONTENT"

        fun createInstance(context: Context,
                           time: Long,
                           bibleTextPair: BibleTextPair,
                           note: String) = ShareDialog().apply {
            arguments = Bundle().apply {
                putString(KEY_INITIAL_CONTENT,
                        contentAsString(context, time, bibleTextPair, note))
            }
        }

    }

    private lateinit var input: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog =
            activity.let { activity ->
                if (activity == null) {
                    throw NullPointerException()
                }
                AlertDialog.Builder(activity)
                        .setTitle(R.string.share_dialog_title)
                        .setNegativeButton(R.string.dialog_cancel, null)
                        .setPositiveButton(R.string.share_dialog_ok, { _, _ ->
                            startActivity(shareIntent(input.text.toString(),
                                    resources.getText(R.string.share_dialog_chooser_title)))
                            dismiss()
                        })
                        .setView(createLayout(activity))
                        .create()
            }

    @SuppressLint("InflateParams")
    private fun createLayout(activity: Activity) =
            activity.layoutInflater.inflate(R.layout.share_dialog, null).apply {
                input = findViewById<EditText>(R.id.share_dialog_input).apply {
                    setText(arguments?.getString(KEY_INITIAL_CONTENT) ?: "")
                }
            }

}