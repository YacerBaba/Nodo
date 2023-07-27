package owner.yacer.nodoproject.data.models

import androidx.room.Embedded
import androidx.room.Relation


data class NoteWithNoteImages (
    @Embedded val note:Note,
    @Relation(
        parentColumn = "noteId",
        entityColumn = "noteId"
    )
    val listOfImgs:List<NoteImage>
    )