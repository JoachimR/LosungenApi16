package de.reiss.android.losungen.note.edit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import de.reiss.android.losungen.R
import de.reiss.android.losungen.architecture.AppActivity
import de.reiss.android.losungen.model.LosungContent
import de.reiss.android.losungen.util.extensions.findFragmentIn
import de.reiss.android.losungen.util.extensions.replaceFragmentIn
import de.reiss.android.losungen.util.extensions.withZeroDayTime
import kotlinx.android.synthetic.main.edit_note_activity.*
import java.util.*

class EditNoteActivity : AppActivity() {

    companion object {

        private const val KEY_TIME = "KEY_TIME"
        private const val KEY_LOSUNG_CONTENT = "KEY_LOSUNG_CONTENT"

        fun createIntent(context: Context, date: Date, losungContent: LosungContent): Intent =
                Intent(context, EditNoteActivity::class.java)
                        .putExtra(KEY_TIME, date.withZeroDayTime().time)
                        .putExtra(KEY_LOSUNG_CONTENT, losungContent)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_note_activity)
        setSupportActionBar(edit_note_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (findFragmentIn(R.id.edit_note_fragment_container) == null) {
            val time = intent.getLongExtra(KEY_TIME, -1L)
            if (time == -1L) {
                throw IllegalStateException("No time given for note")
            }
            val content = intent.getParcelableExtra<LosungContent>(KEY_LOSUNG_CONTENT) ?:
                    throw IllegalStateException("No losung content given for note")

            replaceFragmentIn(
                    container = R.id.edit_note_fragment_container,
                    fragment = EditNoteFragment.createInstance(time, content))
        }
    }

}