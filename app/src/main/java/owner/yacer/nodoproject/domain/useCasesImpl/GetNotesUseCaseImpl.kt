package owner.yacer.nodoproject.domain.useCasesImpl

import owner.yacer.nodoproject.domain.interfaces.LocalRepository
import owner.yacer.nodoproject.domain.useCases.GetNotesUseCase

class GetNotesUseCaseImpl (private val localRepository: LocalRepository): GetNotesUseCase {
    override fun execute() {
        localRepository.getNotesSortedByTime()
    }
}