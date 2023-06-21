package owner.yacer.nodoproject.data.models

import androidx.room.Embedded
import androidx.room.Relation
import java.util.*

data class TodoListWithTasks (
    @Embedded val todoList:TodoList,
    @Relation(
        parentColumn = "id",
        entityColumn = "todoListId",
        )
    val tasks: List<Task>
    )