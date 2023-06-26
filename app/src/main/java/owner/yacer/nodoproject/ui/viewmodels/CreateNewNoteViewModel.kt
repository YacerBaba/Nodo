package owner.yacer.nodoproject.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import owner.yacer.nodoproject.data.models.Note
import owner.yacer.nodoproject.data.models.NoteImage
import owner.yacer.nodoproject.domain.useCases.NotesUseCases.AddNoteImagesUseCase
import owner.yacer.nodoproject.domain.useCases.NotesUseCases.AddNoteUseCase
import javax.inject.Inject

class CreateNewNoteViewModel @Inject constructor(
    private val addNoteUseCase: AddNoteUseCase,
    private val addNoteImagesUseCase: AddNoteImagesUseCase
    ) :
    ViewModel() {

    fun addNote(note: Note):Long = addNoteUseCase.execute(note)


    fun addNoteImage(noteImage: NoteImage){
        addNoteImagesUseCase.execute(noteImage)
    }
}