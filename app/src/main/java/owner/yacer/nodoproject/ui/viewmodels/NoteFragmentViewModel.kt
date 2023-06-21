package owner.yacer.nodoproject.ui.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import owner.yacer.nodoproject.data.models.Note
import owner.yacer.nodoproject.data.repository.LocalRepositoryImpl

class NoteFragmentViewModel : ViewModel(){
    // declare observable data types
    val mutableNotesList = MutableLiveData<List<Note>>()

    fun initContext(context: Context){
        LocalRepositoryImpl.initDatabase(context)
    }

    fun getNotesSortedByTime(){
        CoroutineScope(Dispatchers.IO).launch {
            val notesListFromRepo = LocalRepositoryImpl.getNotesSortedByTime()
            withContext(Dispatchers.Main){
                mutableNotesList.value = notesListFromRepo
            }
        }
    }



}