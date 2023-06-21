package owner.yacer.nodoproject.data.local

import androidx.room.RoomDatabase
import owner.yacer.nodoproject.data.models.Note
import owner.yacer.nodoproject.data.models.Task
import owner.yacer.nodoproject.data.models.TodoList

@androidx.room.Database(entities = [Note::class, TodoList::class, Task::class], version = 1)
abstract class Database : RoomDatabase() {
    // get dao instances for each table
    abstract fun getNoteDao():NoteDao
    abstract fun getTodoListDao():TodoListDao
    abstract fun getTaskDao():TaskDao
}