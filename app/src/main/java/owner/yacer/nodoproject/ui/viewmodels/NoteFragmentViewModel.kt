package owner.yacer.nodoproject.ui.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import owner.yacer.nodoproject.data.models.Note
import owner.yacer.nodoproject.data.models.NoteWithNoteImages
import owner.yacer.nodoproject.domain.useCases.NotesUseCases.GetNotesUseCase
import owner.yacer.nodoproject.domain.useCases.InitDatabaseUseCase
import javax.inject.Inject

class NoteFragmentViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val initDatabaseUseCase: InitDatabaseUseCase
    ) : ViewModel(){
    // declare observable data types
    val mutableNotesList = MutableLiveData<List<NoteWithNoteImages>>()

    fun initContext(context: Context){
        initDatabaseUseCase.execute(context)
    }

    fun getNotesSortedByTime(){
        CoroutineScope(Dispatchers.IO).launch {
            val notesListFromRepo = getNotesUseCase.execute()
            withContext(Dispatchers.Main){
                mutableNotesList.value = notesListFromRepo
            }
        }
    }



}