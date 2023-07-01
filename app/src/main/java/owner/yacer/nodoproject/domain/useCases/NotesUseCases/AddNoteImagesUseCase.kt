package owner.yacer.nodoproject.domain.useCases.NotesUseCases

import owner.yacer.nodoproject.data.models.NoteImage
import owner.yacer.nodoproject.domain.interfaces.LocalRepository
import javax.inject.Inject

class AddNoteImagesUseCase @Inject constructor(val localRepository: LocalRepository) {
    fun execute(noteImage: NoteImage){
        localRepository.addNoteImage(noteImage = noteImage)
    }
}