package owner.yacer.nodoproject.ui.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import owner.yacer.nodoproject.data.models.Task
import owner.yacer.nodoproject.data.models.TodoList
import owner.yacer.nodoproject.data.models.TodoListWithTasks
import owner.yacer.nodoproject.data.repository.LocalRepositoryImpl

class TodoFragmentViewModel:ViewModel() {

    var mutableTodoList = MutableLiveData<List<TodoListWithTasks>>()

    fun initDatabase(context: Context){
        LocalRepositoryImpl.initDatabase(context)
    }

    fun getTodoListsWithTask(){
        CoroutineScope(Dispatchers.IO).launch {
            val result=  LocalRepositoryImpl.getTodoList()
            withContext(Dispatchers.Main){
                mutableTodoList.value = result
            }
        }
    }

    fun addTodoList(todoList: TodoList):Long{
        return LocalRepositoryImpl.addTodoList(todoList)
    }
    fun updateTodoList(todoList: TodoList){
        LocalRepositoryImpl.updateTodoList(todoList)
    }

    fun deleteTodoList(todoList: TodoList){
        LocalRepositoryImpl.deleteTodoList(todoList)
    }

    fun addTasks(tasks: List<Task>){
        LocalRepositoryImpl.addTasks(tasks = tasks)
    }
    fun addTask(task: Task):Long{
        return LocalRepositoryImpl.addTask(task)
    }
    fun updateTask(task:Task){
        LocalRepositoryImpl.updateTask(task)
    }
    fun deleteTask(task:Task){
        LocalRepositoryImpl.deleteTask(task)
    }

}