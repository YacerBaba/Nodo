package owner.yacer.nodoproject.domain.interfaces

import android.content.Context
import owner.yacer.nodoproject.data.models.*

interface LocalRepository {
    fun initDatabase(context: Context)
    fun getNotesSortedByTime():List<NoteWithNoteImages>

    fun addNote(note: Note):Long
    fun deleteNote(note: Note)
    fun updateNote(note: Note)
    fun addNoteImage(noteImage: NoteImage)
    fun getNoteImages(id:Long):List<NoteImage>
    fun deleteNoteImage(noteImage: NoteImage)
    // TodoList Part
    fun getTodoLists():List<TodoListWithTasks>
    fun addTodoList(todoList: TodoList):Long
    fun updateTodoList(todoList: TodoList)
    fun deleteTodoList(todoList: TodoList)

    fun addTasks(tasks:List<Task>)
    fun addTask(task: Task):Long
    fun deleteTask(task: Task)
    fun updateTask(task: Task)

}