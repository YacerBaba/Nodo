package owner.yacer.nodoproject.data.models

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity (foreignKeys = [ForeignKey(
    entity = Note::class,
    parentColumns = ["noteId"],
    childColumns = ["noteId"],
    onDelete = ForeignKey.CASCADE
)], primaryKeys = ["noteId","imgUrl"])
data class NoteImage (
    val noteId:Long,
    val imgUrl:String
)