package owner.yacer.nodoproject.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import owner.yacer.nodoproject.data.models.Note
import owner.yacer.nodoproject.data.repository.LocalRepositoryImpl

class CreateNewNoteViewModel : ViewModel() {

    suspend fun addNote(note: Note){
        var id = LocalRepositoryImpl.addNote(note)
        Log.e("inserted note id :","$id")
    }
}