package de.reiss.android.losungen.note.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import de.reiss.android.losungen.R
import de.reiss.android.losungen.architecture.AppActivity
import de.reiss.android.losungen.model.Note
import de.reiss.android.losungen.util.extensions.findFragmentIn
import de.reiss.android.losungen.util.extensions.replaceFragmentIn
import kotlinx.android.synthetic.main.note_details_activity.*

class NoteDetailsActivity : AppActivity(), ConfirmDeleteDialog.Listener {

    companion object {

        private const val KEY_NOTE = "KEY_NOTE"

        fun createIntent(context: Context, note: Note): Intent =
                Intent(context, NoteDetailsActivity::class.java)
                        .putExtra(KEY_NOTE, note)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_details_activity)
        setSupportActionBar(note_details_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (findNoteDetailsFragment() == null) {
            replaceFragmentIn(
                    container = R.id.note_details_fragment,
                    fragment = NoteDetailsFragment.createInstance(
                            intent.getParcelableExtra(KEY_NOTE))
            )
        }
    }

    override fun onDeleteConfirmed() {
        findNoteDetailsFragment()?.tryDeleteNote()
    }

    private fun findNoteDetailsFragment() =
            findFragmentIn(R.id.note_details_fragment) as? NoteDetailsFragment

}