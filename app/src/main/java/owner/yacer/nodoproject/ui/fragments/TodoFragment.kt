package owner.yacer.nodoproject.ui.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.add_todolist_layout.*
import kotlinx.android.synthetic.main.add_todolist_layout.view.*
import kotlinx.android.synthetic.main.fragment_todo.*
import kotlinx.android.synthetic.main.included_add_todo_layout.*
import kotlinx.coroutines.*
import owner.yacer.nodoproject.R
import owner.yacer.nodoproject.data.models.Task
import owner.yacer.nodoproject.data.models.TodoList
import owner.yacer.nodoproject.data.models.TodoListWithTasks
import owner.yacer.nodoproject.ui.adapters.TodoListAdapter
import owner.yacer.nodoproject.ui.viewmodels.TodoFragmentViewModel
import java.util.Date


class TodoFragment : Fragment(R.layout.fragment_todo) {
    lateinit var todoViewModel: TodoFragmentViewModel
    lateinit var adapter: TodoListAdapter
    lateinit var addTasksDialogBox: Dialog

    var listOfTodoList = listOf<TodoListWithTasks>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        addTasksDialogBox = Dialog(requireContext())
        todoViewModel = ViewModelProvider(this)[TodoFragmentViewModel::class.java]
        todoViewModel.initDatabase(requireContext())
        setupRecyclerView()
        lifecycleScope.async {
            todoViewModel.getTodoListsWithTask()
            todoViewModel.mutableTodoList.observe(this@TodoFragment) {
                listOfTodoList = it
                adapter.todoLists = listOfTodoList.toMutableList()
                Log.e("msg","observer called !!!!")
                Log.e("msg",it.toString())
                adapter.notifyDataSetChanged()
            }
        }

        fab_addNewTodoList.setOnClickListener {
            showAddTasksDialogBox()

            addTasksDialogBox.addTodoList_done.setOnClickListener {
                val todoListTitle = addTasksDialogBox.addTodoList_title.text.toString()
                val currentTime = Date().time / 1000
                val todoList = TodoList(todoListTitle, false, currentTime)
                lifecycleScope.launch {
                    val tasksList = mutableListOf<Task>()
                    for (i in 0 until addTasksDialogBox.parentLayout.childCount) {
                        val childLayout =
                            addTasksDialogBox.parentLayout.getChildAt(i) as LinearLayout
                        val isChecked = (childLayout.getChildAt(0) as CheckBox).isChecked
                        val taskName = (childLayout.getChildAt(1) as EditText).text.toString()
                        if (taskName.isNotEmpty()) {
                            Log.e("msg", "$taskName : $isChecked")
                            var todoListId:Long = -1
                            var task = Task(taskName, isChecked, todoListId)
                            tasksList.add(task)
                        }
                    }
                    if (tasksList.isNotEmpty()) {
                        withContext(Dispatchers.IO) {
                            val addedTodoListID = todoViewModel.addTodoList(todoList)
                            Log.e("msgTodoListID", addedTodoListID.toString())
                            tasksList.forEach { task ->
                                task.todoListId = addedTodoListID
                            }
                            todoViewModel.addTasks(tasks = tasksList)
                            withContext(Dispatchers.Main) {
                                updateUI()
                                addTasksDialogBox.dismiss()
                            }
                        }
                    }else{
                        addTasksDialogBox.dismiss()
                    }
                }
            }

            addTasksDialogBox.addTodoList_cancel.setOnClickListener {
                addTasksDialogBox.dismiss()
            }
        }

        todos_searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                var searchTodoListResult = mutableListOf<TodoListWithTasks>()
                listOfTodoList.forEach { todoList ->
                    if (todoList.todoList.title.lowercase().contains(newText!!.lowercase())) {
                        searchTodoListResult.add(todoList)
                    } else {
                        for (task in todoList.tasks) {
                            if (task.body.lowercase().contains(newText.lowercase())) {
                                searchTodoListResult.add(todoList)
                                break
                            }
                        }
                    }
                }
                adapter.todoLists = searchTodoListResult
                adapter.notifyDataSetChanged()
                return true
            }
        })

    }

    private fun updateUI() {
        todoViewModel.getTodoListsWithTask()
    }

    private fun setupRecyclerView() {
        adapter = TodoListAdapter(requireContext(), todoViewModel)
        todoListRecyclerView.adapter = adapter
        todoListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun showAddTasksDialogBox() {
        addTasksDialogBox.setContentView(R.layout.add_todolist_layout)
        addTasksDialogBox.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        addTasksDialogBox.show()

        Log.e("msg", "show soft input executed")
        addTasksDialogBox.addTodoList_task1.requestFocus()

        val window = addTasksDialogBox.window
        window?.setGravity(Gravity.BOTTOM)
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

        try {
            setEnterDeleteListenerToEditText(addTasksDialogBox.parentLayout.childLayout as LinearLayout)
        } catch (e: Exception) {
            Log.e("msgException", e.message!!)
        }

    }

    private fun setEnterDeleteListenerToEditText(childLayout: LinearLayout) {
        val parentLayout = addTasksDialogBox.parentLayout
        var checkbox = childLayout.getChildAt(0) as CheckBox
        var editText = childLayout.getChildAt(1) as EditText
        editText.setOnEditorActionListener { v, actionId, event ->
            if (editText.text.isNotEmpty() &&
                (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT)
            ) {
                var childLayout = LayoutInflater.from(requireContext())
                    .inflate(R.layout.included_add_todo_layout, null) as LinearLayout
                var addTask_edit = childLayout.getChildAt(1) as EditText
                addTask_edit.requestFocus()
                // set the enter listener to the new created layout
                setEnterDeleteListenerToEditText(childLayout)
                parentLayout.addView(childLayout)
                parentLayout.getChildAt(parentLayout.childCount - 1)
                true
            } else {
                false
            }
        }
        editText.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {

                if (editText.text.toString().isEmpty() && parentLayout.childCount > 1) {
                    parentLayout.removeView(childLayout)
                }
                true
            } else
                false
        }
    }
}