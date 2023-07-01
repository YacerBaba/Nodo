package owner.yacer.nodoproject.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import owner.yacer.nodoproject.data.models.Note
import owner.yacer.nodoproject.data.models.NoteWithNoteImages

@Dao
interface NoteDao {
    @Query("SELECT * from Note order by time DESC")
    fun getNotesSortedByTime():List<NoteWithNoteImages>

    @Insert
    fun addNote(note:Note):Long

    @Delete
    fun deleteNote(note:Note)

    @Update
    fun updateNote(note:Note)
}