package de.reiss.android.losungen.note.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import de.reiss.android.losungen.R
import de.reiss.android.losungen.architecture.AppActivity
import de.reiss.android.losungen.databinding.NoteDetailsActivityBinding
import de.reiss.android.losungen.model.Note
import de.reiss.android.losungen.util.extensions.findFragmentIn
import de.reiss.android.losungen.util.extensions.replaceFragmentIn

class NoteDetailsActivity : AppActivity(), ConfirmDeleteDialog.Listener {

    companion object {

        private const val KEY_NOTE = "KEY_NOTE"

        fun createIntent(context: Context, note: Note): Intent =
            Intent(context, NoteDetailsActivity::class.java)
                .putExtra(KEY_NOTE, note)

    }

    private lateinit var binding: NoteDetailsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NoteDetailsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.noteDetailsToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (findNoteDetailsFragment() == null) {
            replaceFragmentIn(
                container = R.id.note_details_fragment,
                fragment = NoteDetailsFragment.createInstance(
                    intent.getParcelableExtra(KEY_NOTE)!!
                )
            )
        }
    }

    override fun onDeleteConfirmed() {
        findNoteDetailsFragment()?.tryDeleteNote()
    }

    private fun findNoteDetailsFragment() =
        findFragmentIn(R.id.note_details_fragment) as? NoteDetailsFragment

}