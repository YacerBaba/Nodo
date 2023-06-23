package owner.yacer.nodoproject.domain.useCases

import android.content.Context
import owner.yacer.nodoproject.domain.interfaces.LocalRepository
import javax.inject.Inject

class InitDatabaseUseCase @Inject constructor(private val localRepository: LocalRepository) {
    fun execute(context:Context) {
        localRepository.initDatabase(context)
    }
}