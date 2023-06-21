package owner.yacer.nodoproject.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys =  [
    ForeignKey(
        entity = TodoList::class,
        parentColumns = ["id"],
        childColumns = ["todoListId"],
        onDelete = ForeignKey.CASCADE // Optional: Define the behavior on deletion
    )
],)
data class Task(
    var body:String,
    var checked:Boolean,
    var todoListId:Long,
    @PrimaryKey(autoGenerate = true)
    var tid:Long = 0,
)
