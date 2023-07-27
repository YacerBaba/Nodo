package owner.yacer.nodoproject.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class TodoList (
    var title:String,
    var completed:Boolean,
    var date: Long,
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
)