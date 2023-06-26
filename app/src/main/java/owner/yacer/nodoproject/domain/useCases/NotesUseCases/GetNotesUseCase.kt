package owner.yacer.nodoproject.domain.useCases.NotesUseCases

import owner.yacer.nodoproject.data.models.Note
import owner.yacer.nodoproject.data.models.NoteWithNoteImages
import owner.yacer.nodoproject.domain.interfaces.LocalRepository
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(private val localRepository: LocalRepository) {
    fun execute() : List<NoteWithNoteImages> {
        return localRepository.getNotesSortedByTime()
    }
}