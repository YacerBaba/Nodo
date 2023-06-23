package owner.yacer.nodoproject.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import owner.yacer.nodoproject.data.models.Note
import owner.yacer.nodoproject.domain.useCases.NotesUseCases.AddNoteUseCase
import javax.inject.Inject

class CreateNewNoteViewModel @Inject constructor(private val addNoteUseCase: AddNoteUseCase) :
    ViewModel() {

    fun addNote(note: Note) {
        var id = addNoteUseCase.execute(note)
        Log.e("inserted note id :", "$id")
    }
}