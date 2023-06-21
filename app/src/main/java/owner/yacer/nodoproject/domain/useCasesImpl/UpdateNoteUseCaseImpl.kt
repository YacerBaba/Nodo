package owner.yacer.nodoproject.domain.useCasesImpl

import owner.yacer.nodoproject.data.models.Note
import owner.yacer.nodoproject.domain.interfaces.LocalRepository
import owner.yacer.nodoproject.domain.useCases.UpdateNoteUseCase

class UpdateNoteUseCaseImpl (private val localRepository: LocalRepository) : UpdateNoteUseCase {
    override fun execute(note: Note) {
        localRepository.updateNote(note)
    }
}