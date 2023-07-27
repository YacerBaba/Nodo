package owner.yacer.nodoproject.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import owner.yacer.nodoproject.data.models.TodoList
import owner.yacer.nodoproject.data.models.TodoListWithTasks

@Dao
interface TodoListDao {
    @Insert
    fun addTodoList(todoList:TodoList):Long

    @Delete
    fun deleteTodoList(todoList: TodoList)

    @Query("SELECT * from TodoList order by date Desc")
    fun getTodoListWithTasks():List<TodoListWithTasks>

    @Update
    fun updateTodoList(todoList: TodoList)
}