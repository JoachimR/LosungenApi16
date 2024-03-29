package de.reiss.android.losungen.note.edit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import de.reiss.android.losungen.R
import de.reiss.android.losungen.architecture.AppActivity
import de.reiss.android.losungen.databinding.EditNoteActivityBinding
import de.reiss.android.losungen.model.BibleTextPair
import de.reiss.android.losungen.util.extensions.findFragmentIn
import de.reiss.android.losungen.util.extensions.replaceFragmentIn
import de.reiss.android.losungen.util.extensions.withZeroDayTime
import java.util.*

class EditNoteActivity : AppActivity() {

    companion object {

        private const val KEY_TIME = "KEY_TIME"
        private const val KEY_BIBLE_TEXT_PAIR = "KEY_BIBLE_TEXT_PAIR"

        fun createIntent(
            context: Context,
            date: Date,
            bibleTextPair: BibleTextPair
        ): Intent =
            Intent(context, EditNoteActivity::class.java)
                .putExtra(KEY_TIME, date.withZeroDayTime().time)
                .putExtra(KEY_BIBLE_TEXT_PAIR, bibleTextPair)

    }

    private lateinit var binding: EditNoteActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditNoteActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.editNoteToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (findFragmentIn(R.id.edit_note_fragment_container) == null) {
            val time = intent.getLongExtra(KEY_TIME, -1L)
            if (time == -1L) {
                throw IllegalStateException("No time given for note")
            }
            val bibleTextPair = intent.getParcelableExtra<BibleTextPair>(KEY_BIBLE_TEXT_PAIR)
                ?: throw IllegalStateException("No losung content given for note")

            replaceFragmentIn(
                container = R.id.edit_note_fragment_container,
                fragment = EditNoteFragment.createInstance(time, bibleTextPair)
            )
        }
    }

}