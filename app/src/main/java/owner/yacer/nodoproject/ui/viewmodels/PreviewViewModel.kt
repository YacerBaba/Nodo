package owner.yacer.nodoproject.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import owner.yacer.nodoproject.data.models.Note
import owner.yacer.nodoproject.data.repository.LocalRepositoryImpl

class PreviewViewModel : ViewModel() {

    fun updateNote(note: Note) {
        LocalRepositoryImpl.updateNote(note)
    }

    fun deleteNote(note: Note) {
        CoroutineScope(Dispatchers.IO).launch {
            LocalRepositoryImpl.deleteNote(note)
        }
    }
}