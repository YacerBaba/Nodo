package owner.yacer.nodoproject.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    val title: String,
    val body: String,
    val time: Long,
    val img :ByteArray?=null,
    val bgcolor:String? = null,
    @PrimaryKey(autoGenerate = true)
    val noteId: Int = 0
)