package owner.yacer.nodoproject.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import owner.yacer.nodoproject.data.models.NoteImage

@Dao
interface NoteImageDao {
    @Insert
    fun addNoteImage(noteImage: NoteImage)

    @Query("select * from NoteImage where noteId = :id")
    fun getNoteImages(id:Int):List<NoteImage>
}