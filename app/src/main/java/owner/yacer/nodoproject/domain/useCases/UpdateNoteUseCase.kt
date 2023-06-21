package owner.yacer.nodoproject.domain.useCases

import owner.yacer.nodoproject.data.models.Note

interface UpdateNoteUseCase {
    fun execute(note: Note)
}