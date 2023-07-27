package owner.yacer.nodoproject.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import owner.yacer.nodoproject.data.models.*
import owner.yacer.nodoproject.domain.useCases.InitDatabaseUseCase
import owner.yacer.nodoproject.domain.useCases.NotesUseCases.DeleteNoteImageUseCase
import owner.yacer.nodoproject.domain.useCases.NotesUseCases.DeleteNoteUseCase
import owner.yacer.nodoproject.domain.useCases.TodosUseCases.DeleteTodoListUseCase
import owner.yacer.nodoproject.domain.useCases.TodosUseCases.TaskUseCases
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val deleteTodoListUseCase: DeleteTodoListUseCase,
    private val taskUseCases: TaskUseCases,
    private val initDatabaseUseCase: InitDatabaseUseCase,
    private val deleteNoteImageUseCase: DeleteNoteImageUseCase
    ) : ViewModel(){

    fun deleteNote(note: Note){
        deleteNoteUseCase.execute(note)
    }

    fun deleteTodoList(todoList:TodoList){
        deleteTodoListUseCase.execute(todoList)
    }

    fun deleteTask(task:Task){
        taskUseCases.deleteTask(task)
    }

    fun initDatabase(context: Context){
        initDatabaseUseCase.execute(context)
    }

    fun deleteNoteImage(noteImage: NoteImage){
        deleteNoteImageUseCase.execute(noteImage)
    }
}