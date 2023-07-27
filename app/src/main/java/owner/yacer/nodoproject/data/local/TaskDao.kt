package owner.yacer.nodoproject.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import owner.yacer.nodoproject.data.models.Task

@Dao
interface TaskDao {

    @Delete
    fun deleteTask(task: Task)

    @Insert
    fun addTasks(tasks: List<Task>)

    @Insert
    fun addTask(task: Task):Long

    @Update
    fun updateTask(task: Task)
}