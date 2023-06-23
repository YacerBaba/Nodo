package owner.yacer.nodoproject.domain.useCases.TodosUseCases

import owner.yacer.nodoproject.data.models.TodoList
import owner.yacer.nodoproject.domain.interfaces.LocalRepository
import javax.inject.Inject

class DeleteTodoListUseCase @Inject constructor(private val localRepository: LocalRepository) {
    fun execute(todoList: TodoList){
        localRepository.deleteTodoList(todoList)
    }
}