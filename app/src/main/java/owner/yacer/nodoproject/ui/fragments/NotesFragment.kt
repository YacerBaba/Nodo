package owner.yacer.nodoproject.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.fragment_notes.*
import owner.yacer.nodoproject.R
import owner.yacer.nodoproject.data.models.Note
import owner.yacer.nodoproject.data.models.NoteWithNoteImages
import owner.yacer.nodoproject.domain.di.DaggerMyComponent
import owner.yacer.nodoproject.ui.activities.CreateNewNoteActivity
import owner.yacer.nodoproject.ui.adapters.NoteAdapter
import owner.yacer.nodoproject.ui.viewmodels.NoteFragmentViewModel

class NotesFragment : Fragment(R.layout.fragment_notes) {
    lateinit var notesAdapter: NoteAdapter

    lateinit var noteViewModel: NoteFragmentViewModel
    var notesList = listOf<NoteWithNoteImages>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val component = DaggerMyComponent.create()
        noteViewModel = component.getNoteViewModel()
        noteViewModel.initContext(requireContext())
        setupRecyclerView()
        noteViewModel.getNotesSortedByTime()
        noteViewModel.mutableNotesList.observe(requireActivity()) {
            notesList = it
            Log.e("msgNotesList",notesList.toString())
            notesAdapter.notesList = notesList.toMutableList()
            notesAdapter.notifyDataSetChanged()
        }
        fab_addNewNote.setOnClickListener {
            Intent(requireContext(), CreateNewNoteActivity::class.java).also {
                startActivity(it)
                requireActivity().overridePendingTransition(
                    R.anim.slide_in_right, R.anim.slide_out_left
                )
            }
        }

        notes_searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                var searchResultList = mutableListOf<NoteWithNoteImages>()
                notesList.forEach { note ->
                    if (note.note.title.lowercase().contains(newText!!.lowercase())
                        || note.note.body.lowercase().contains(newText.lowercase())
                    ) {
                        searchResultList.add(note)
                    }
                }
                notesAdapter.notesList = searchResultList
                notesAdapter.notifyDataSetChanged()
                return true
            }

        })
    }

    private fun setupRecyclerView() {
        notesAdapter = NoteAdapter(requireContext())
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        notesRecyclerView.adapter = notesAdapter
        notesRecyclerView.layoutManager = layoutManager
    }

    override fun onResume() {
        super.onResume()
        noteViewModel.getNotesSortedByTime()
    }
}