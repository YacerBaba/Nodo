package owner.yacer.nodoproject.data.repository

import android.content.Context
import androidx.room.Room
import owner.yacer.nodoproject.data.local.*
import owner.yacer.nodoproject.data.models.*
import owner.yacer.nodoproject.domain.interfaces.LocalRepository

object LocalRepositoryImpl : LocalRepository {
    lateinit var db:Database
    lateinit var noteDao: NoteDao
    lateinit var todoListDao:TodoListDao
    lateinit var taskDao:TaskDao
    lateinit var noteImagesDao:NoteImageDao
    override fun initDatabase(context:Context){
        db = Room.databaseBuilder(context,Database::class.java,"nodo_db").build()
        noteDao = db.getNoteDao()
        todoListDao = db.getTodoListDao()
        taskDao = db.getTaskDao()
        noteImagesDao = db.getNoteImagesDao()
    }
    override fun getNotesSortedByTime():List<NoteWithNoteImages> = noteDao.getNotesSortedByTime()

    override fun addNote(note:Note):Long{
        return noteDao.addNote(note)
    }
    override fun deleteNote(note:Note){
        noteDao.deleteNote(note)
    }
    override fun updateNote(note:Note){
        noteDao.updateNote(note)
    }

    override fun addNoteImage(noteImage: NoteImage){
        noteImagesDao.addNoteImage(noteImage)
    }

    override fun getNoteImages(id: Long): List<NoteImage> =
        noteImagesDao.getNoteImages(id)

    override fun deleteNoteImage(noteImage: NoteImage) {
        noteImagesDao.deleteNoteImage(noteImage)
    }
    // TodoList Part
    override fun getTodoLists():List<TodoListWithTasks> = todoListDao.getTodoListWithTasks()
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