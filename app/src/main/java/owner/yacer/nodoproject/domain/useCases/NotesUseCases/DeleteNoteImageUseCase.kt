package owner.yacer.nodoproject.domain.useCases.NotesUseCases

import owner.yacer.nodoproject.data.models.NoteImage
import owner.yacer.nodoproject.domain.interfaces.LocalRepository
import javax.inject.Inject

class DeleteNoteImageUseCase @Inject constructor(
    private val localRepository: LocalRepository) {

    fun execute(noteImage: NoteImage){
        localRepository.deleteNoteImage(noteImage)
    }
}