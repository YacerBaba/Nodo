package owner.yacer.nodoproject.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import owner.yacer.nodoproject.data.models.Note
import owner.yacer.nodoproject.data.models.NoteImage
import owner.yacer.nodoproject.domain.useCases.NotesUseCases.DeleteNoteUseCase
import owner.yacer.nodoproject.domain.useCases.NotesUseCases.GetNoteImagesUseCase
import owner.yacer.nodoproject.domain.useCases.NotesUseCases.UpdateNoteUseCase
import javax.inject.Inject

class PreviewViewModel @Inject constructor(
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val getNoteImagesUseCase: GetNoteImagesUseCase
    ) : ViewModel() {

    fun updateNote(note: Note) {
        updateNoteUseCase.execute(note)
    }

    fun deleteNote(note: Note) {
        CoroutineScope(Dispatchers.IO).launch {
            deleteNoteUseCase.execute(note)
        }
    }

    fun getNoteImages(id:Int):List<NoteImage> = getNoteImagesUseCase.execute(id)
}