package owner.yacer.nodoproject.ui.adapters

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.included_add_todo_layout.view.*
import kotlinx.coroutines.*
import owner.yacer.nodoproject.R
import owner.yacer.nodoproject.data.models.Task
import owner.yacer.nodoproject.data.models.TodoListWithTasks
import owner.yacer.nodoproject.ui.viewmodels.TodoFragmentViewModel

class PreviewTasksAdapter(
    private val context: Context,
    private val viewModel: TodoFragmentViewModel,
    private val mutableTasksList: MutableList<Task>
) : RecyclerView.Adapter<PreviewTasksAdapter.ViewHolder>() {
    val todoListId = mutableTasksList[0].todoListId

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var taskEditText = view.addTodoList_task1
        var isAlreadyExist = true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.included_add_todo_layout,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = mutableTasksList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.e("msg","task id : ${mutableTasksList[position].tid}")
        if (mutableTasksList[position].body.isNotEmpty())
            holder.taskEditText.setText(mutableTasksList[position].body)
        else
            holder.taskEditText.hint = "Type something todo"

        setEnterDeleteListenerToEditText(holder, position)
    }

    private fun setEnterDeleteListenerToEditText(holder: ViewHolder,position: Int) {
        val currentTask = mutableTasksList[position]

        holder.taskEditText.setOnEditorActionListener { v, actionId, event ->
            if (holder.taskEditText.text.isNotEmpty() &&
                (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT)
            ) {
                addNewRow()
                true
            } else {
                false
            }
        }

        holder.taskEditText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {

                if ((holder.taskEditText.text.toString().isEmpty() ||
                            holder.taskEditText.text.toString().length == 1) && this.itemCount > 1) {
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.deleteTask(currentTask)
                        viewModel.getTodoListsWithTask()
                        withContext(Dispatchers.Main){
                            mutableTasksList.removeAt(position)
                            notifyItemRemoved(position)
                        }
                    }
                }
                true
            } else
                false
        }

        holder.taskEditText.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                Log.e("msg","afterTextChanged executed : ${s.toString()}")

                mutableTasksList[position].body = s.toString()
                CoroutineScope(Dispatchers.IO).launch {

                    Log.e("msg","currentTask body : ${mutableTasksList[position].body}")
                    viewModel.updateTask(mutableTasksList[position])
                    viewModel.getTodoListsWithTask()
                    this.cancel()
                }
            }

        })
        Log.e("msg","TextChangeListener added to view ")
    }

    private fun addNewRow(){
        val task = Task("", false, todoListId)
        CoroutineScope(Dispatchers.IO).launch{
            var addedTaskId = viewModel.addTask(task)
            withContext(Dispatchers.Main){
                Log.e("msg","added task id :$addedTaskId")
                task.tid = addedTaskId
                mutableTasksList.add(task)
                notifyItemInserted(mutableTasksList.size-1)
            }
        }

    }

}