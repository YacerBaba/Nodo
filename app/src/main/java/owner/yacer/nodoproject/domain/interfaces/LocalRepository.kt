package owner.yacer.nodoproject.domain.interfaces

import android.content.Context
import owner.yacer.nodoproject.data.models.Note
import owner.yacer.nodoproject.data.models.Task
import owner.yacer.nodoproject.data.models.TodoList
import owner.yacer.nodoproject.data.models.TodoListWithTasks

interface LocalRepository {
    fun initDatabase(context: Context)
    fun getNotesSortedByTime():List<Note>

    fun addNote(note: Note):Long
    fun deleteNote(note: Note)
    fun updateNote(note: Note)

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