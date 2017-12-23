package de.reiss.android.losungen.note.list


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import de.reiss.android.losungen.App
import de.reiss.android.losungen.R
import de.reiss.android.losungen.architecture.AppFragment
import de.reiss.android.losungen.architecture.AsyncLoad
import de.reiss.android.losungen.model.Note
import de.reiss.android.losungen.note.details.NoteDetailsActivity
import kotlinx.android.synthetic.main.note_list_fragment.*


class NoteListFragment : AppFragment<NoteListViewModel>(R.layout.note_list_fragment),
        NoteClickListener {

    companion object {

        fun createInstance() = NoteListFragment()

    }

    private val listItemAdapter = NoteListItemAdapter(noteClickListener = this)

    override fun initViews(layout: View) {
        with(note_list_recycler_view) {
            layoutManager = LinearLayoutManager(context)
            adapter = listItemAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        note_list_swipe_to_refresh.setOnRefreshListener {
            tryLoadNotes()
        }
    }

    override fun defineViewModelProvider(): ViewModelProvider =
            ViewModelProviders.of(this, NoteListViewModel.Factory(
                    App.component.noteListRepository))

    override fun defineViewModel(): NoteListViewModel =
            loadViewModelProvider().get(NoteListViewModel::class.java)

    override fun initViewModelObservers() {
        viewModel.notesLiveData().observe(this, Observer<AsyncLoad<FilteredNotes>> {
            updateUi()
        })
    }

    override fun onResume() {
        super.onResume()
        tryLoadNotes()
    }

    override fun onNoteClicked(note: Note) {
        activity?.let {
            it.startActivity(NoteDetailsActivity.createIntent(
                    context = it,
                    note = note))
        }
    }

    fun applyFilter(query: String) {
        tryRefreshFilter(query)
    }

    private fun tryLoadNotes() {
        if (viewModel.isLoadingNotes().not()) {
            viewModel.loadNotes()
        }
    }

    private fun tryRefreshFilter(query: String) {
        if (viewModel.isLoadingNotes().not()) {
            viewModel.applyNewFilter(query)
        }
    }

    private fun updateUi() {
        if (viewModel.isLoadingNotes()) {
            note_list_swipe_to_refresh.isRefreshing = true
        } else {
            note_list_swipe_to_refresh.isRefreshing = false
            val filteredNotes = viewModel.notes()

            val listItems = NoteListBuilder.buildList(filteredNotes.filteredItems)
            if (listItems.isEmpty()) {
                note_list_recycler_view.visibility = GONE
                note_list_no_notes.visibility = VISIBLE
                note_list_no_notes_text.text =
                        if (filteredNotes.allItems.isEmpty()) {
                            getString(R.string.no_notes)
                        } else {
                            getString(R.string.no_notes_for_filter, filteredNotes.query)
                        }
            } else {
                note_list_no_notes.visibility = GONE
                note_list_recycler_view.visibility = VISIBLE
                listItemAdapter.updateContent(listItems)
            }
        }
    }

}