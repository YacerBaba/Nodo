package owner.yacer.nodoproject.domain.useCases.TodosUseCases

import owner.yacer.nodoproject.data.models.TodoListWithTasks
import owner.yacer.nodoproject.domain.interfaces.LocalRepository
import javax.inject.Inject

class GetTodoListsUseCase @Inject constructor(private val localRepository: LocalRepository) {
    fun execute():List<TodoListWithTasks>{
        return localRepository.getTodoLists()
    }
}