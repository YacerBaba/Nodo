package owner.yacer.nodoproject.domain.useCases.TodosUseCases

import owner.yacer.nodoproject.data.models.Task
import owner.yacer.nodoproject.domain.interfaces.LocalRepository
import javax.inject.Inject

class TaskUseCases @Inject constructor(private val localRepository: LocalRepository) {

    fun addTasks(tasks:List<Task>){
        localRepository.addTasks(tasks)
    }
    fun addTask(task: Task):Long{
        return localRepository.addTask(task)
    }
    fun updateTask(task:Task){
        localRepository.updateTask(task)
    }
    fun deleteTask(task: Task){
        localRepository.deleteTask(task)
    }
}