package owner.yacer.nodoproject.ui.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import owner.yacer.nodoproject.data.models.Task
import owner.yacer.nodoproject.data.models.TodoList
import owner.yacer.nodoproject.data.models.TodoListWithTasks
import owner.yacer.nodoproject.data.repository.LocalRepositoryImpl
import owner.yacer.nodoproject.domain.interfaces.LocalRepository
import owner.yacer.nodoproject.domain.useCases.InitDatabaseUseCase
import owner.yacer.nodoproject.domain.useCases.TodosUseCases.*
import javax.inject.Inject

class TodoFragmentViewModel @Inject constructor(
    private val initDatabaseUseCase: InitDatabaseUseCase,
    private val addTodoListUseCase: AddTodoListUseCase,
    private val getTodoListsUseCase: GetTodoListsUseCase,
    private val deleteTodoListUseCase : DeleteTodoListUseCase,
    private val updateTodoListUseCase: UpdateTodoListUseCase,
    private val taskUseCases: TaskUseCases
) : ViewModel() {

    var mutableTodoList = MutableLiveData<List<TodoListWithTasks>>()

    fun initDatabase(context: Context) {
        initDatabaseUseCase.execute(context)
    }

    fun getTodoListsWithTask() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO){
                getTodoListsUseCase.execute()
            }
            mutableTodoList.value = result
        }
    }

    fun addTodoList(todoList: TodoList): Long {
        return addTodoListUseCase.execute(todoList)
    }

    fun updateTodoList(todoList: TodoList) {
        updateTodoListUseCase.execute(todoList)
    }

    fun deleteTodoList(todoList: TodoList) {
        deleteTodoListUseCase.execute(todoList)
    }

    fun addTasks(tasks: List<Task>) {
        taskUseCases.addTasks(tasks = tasks)
    }

    fun addTask(task: Task): Long {
        return taskUseCases.addTask(task)
    }

    fun updateTask(task: Task) {
        taskUseCases.updateTask(task)
    }

    fun deleteTask(task: Task) {
        taskUseCases.deleteTask(task)
    }

}