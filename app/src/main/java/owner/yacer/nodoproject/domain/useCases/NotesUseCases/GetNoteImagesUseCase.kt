package owner.yacer.nodoproject.domain.useCases.NotesUseCases

import owner.yacer.nodoproject.data.models.NoteImage
import owner.yacer.nodoproject.domain.interfaces.LocalRepository
import javax.inject.Inject

class GetNoteImagesUseCase @Inject constructor(val localRepository: LocalRepository) {
    fun execute(id:Long):List<NoteImage> = localRepository.getNoteImages(id)
}