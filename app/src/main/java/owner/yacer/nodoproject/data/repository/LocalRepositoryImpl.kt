package owner.yacer.nodoproject.data.repository

import android.content.Context
import androidx.room.Room
import owner.yacer.nodoproject.data.local.Database
import owner.yacer.nodoproject.data.local.NoteDao
import owner.yacer.nodoproject.data.local.TaskDao
import owner.yacer.nodoproject.data.local.TodoListDao
import owner.yacer.nodoproject.data.models.Note
import owner.yacer.nodoproject.data.models.Task
import owner.yacer.nodoproject.data.models.TodoList
import owner.yacer.nodoproject.data.models.TodoListWithTasks
import owner.yacer.nodoproject.domain.interfaces.LocalRepository

object LocalRepositoryImpl : LocalRepository {
    lateinit var db:Database
    lateinit var noteDao: NoteDao
    lateinit var todoListDao:TodoListDao
    lateinit var taskDao:TaskDao

    override fun initDatabase(context:Context){
        db = Room.databaseBuilder(context,Database::class.java,"nodo_db").build()
        noteDao = db.getNoteDao()
        todoListDao = db.getTodoListDao()
        taskDao = db.getTaskDao()
    }
    override fun getNotesSortedByTime():List<Note> = noteDao.getNotesSortedByTime()

    override fun addNote(note:Note):Long{
        return noteDao.addNote(note)
    }
    override fun deleteNote(note:Note){
        noteDao.deleteNote(note)
    }
    override fun updateNote(note:Note){
        noteDao.updateNote(note)
    }

    // TodoList Part
    override fun getTodoList():List<TodoListWithTasks> = todoListDao.getTodoListWithTasks()
    override fun addTodoList(todoList: TodoList):Long{
        return todoListDao.addTodoList(todoList)
    }

    override fun updateTodoList(todoList: TodoList){
        todoListDao.updateTodoList(todoList)
    }

    override fun deleteTodoList(todoList:TodoList){
        todoListDao.deleteTodoList(todoList)
    }


    override fun addTasks(tasks:List<Task>){
        taskDao.addTasks(tasks)
    }
    override fun addTask(task: Task):Long{
        return taskDao.addTask(task)
    }
    override fun deleteTask(task: Task){
        taskDao.deleteTask(task)
    }
    override fun updateTask(task: Task){
        taskDao.updateTask(task)
    }



}