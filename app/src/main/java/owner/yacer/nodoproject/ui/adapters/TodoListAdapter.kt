package owner.yacer.nodoproject.ui.adapters

import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_todos_item.view.*
import kotlinx.android.synthetic.main.preview_todolist_layout.*
import kotlinx.android.synthetic.main.todo_item.view.*
import kotlinx.android.synthetic.main.toolbar_delete.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.withContext
import owner.yacer.nodoproject.R
import owner.yacer.nodoproject.app.MainActivity
import owner.yacer.nodoproject.data.models.Task
import owner.yacer.nodoproject.data.models.TodoList
import owner.yacer.nodoproject.data.models.TodoListWithTasks
import owner.yacer.nodoproject.ui.viewmodels.TodoFragmentViewModel

class TodoListAdapter(var context: Context, private val viewModel: TodoFragmentViewModel) :
    RecyclerView.Adapter<TodoListAdapter.TodoListViewHolder>() {
    var isSelectModeEnabled = false
    private var selectedTodoLists = mutableListOf<TodoListWithTasks>()
    var todoLists: MutableList<TodoListWithTasks> = mutableListOf()

    inner class TodoListViewHolder(view: View) : ViewHolder(view) {
        var title = view.todolist_title
        var completed = view.todolist_check
        var tasks_rv = view.todolist_rv
        var todoList_hideShow = view.todolist_btn_hideShow
        var selectCheckBox = view.todolist_selection
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.list_todos_item,
            parent,
            false
        )
        return TodoListViewHolder(view)
    }


    override fun onBindViewHolder(holder: TodoListViewHolder, position: Int) {
        val activity = (context as MainActivity)
        if (todoLists[position].todoList.title.isNotEmpty())
            holder.title.text = todoLists[position].todoList.title
        else {
            holder.title.text = null
            holder.title.hint = "Add Title to your todo list"
        }
        holder.completed.isChecked = todoLists[position].todoList.completed
        if (isSelectModeEnabled) {
            holder.selectCheckBox.visibility = View.VISIBLE
            activity.appViewPager.isUserInputEnabled = false
        } else{
            holder.selectCheckBox.isChecked = false
            holder.selectCheckBox.visibility = View.GONE
            activity.appViewPager.isUserInputEnabled = true
        }

        val taskAdapter = TasksAdapter(this, holder, todoLists[position].tasks, viewModel)
        holder.tasks_rv.adapter = taskAdapter
        holder.tasks_rv.layoutManager = LinearLayoutManager(holder.tasks_rv.context)

        // animations part :

        val rotateDownAnimation =
            ObjectAnimator.ofFloat(holder.todoList_hideShow, "rotation", 0f, 180f)
        val rotateUpAnimation =
            ObjectAnimator.ofFloat(holder.todoList_hideShow, "rotation", 180f, 0f)

        holder.todoList_hideShow.setOnClickListener {
            if (rotateDownAnimation.isRunning || rotateUpAnimation.isRunning) {
                return@setOnClickListener
            }
            val isExpanded = holder.tasks_rv.isVisible
            if (isExpanded) {
                rotateDownAnimation.start()
                holder.tasks_rv.visibility = View.GONE
            } else {
                rotateUpAnimation.start()
                holder.tasks_rv.visibility = View.VISIBLE
            }
        }
        //

        holder.itemView.setOnClickListener {

            if (isSelectModeEnabled) {
                holder.selectCheckBox.isChecked = !holder.selectCheckBox.isChecked
                if (holder.selectCheckBox.isChecked) {
                    var number =
                        activity.toolbarDeleteLayout.toolbarDelete_itemSelected.text.split(
                            " "
                        )[0].toInt()
                    number++

                    activity.toolbarDeleteLayout.toolbarDelete_itemSelected.text = "$number item selected"
                    selectedTodoLists.add(todoLists[position])
                } else {
                    selectedTodoLists.remove(todoLists[position])
                    var number =
                        activity.toolbarDeleteLayout.toolbarDelete_itemSelected.text.split(
                            " "
                        )[0].toInt()
                    number--
                    activity.toolbarDeleteLayout.toolbarDelete_itemSelected.text = "$number item selected"
                }

            } else {
                // preview the note
                Log.e("msg", "item view clicked")
                val dialog = Dialog(context)
                dialog.setContentView(R.layout.preview_todolist_layout)
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog.previewTodoList_title.setText(todoLists[position].todoList.title)
                val mutableTasksList = todoLists[position].tasks.toMutableList()
                val previewTasksAdapter = PreviewTasksAdapter(context, viewModel, mutableTasksList)
                dialog.previewTodoList_rv.adapter = previewTasksAdapter
                dialog.previewTodoList_rv.layoutManager = LinearLayoutManager(context)
                dialog.show()

            }
        }

        holder.itemView.setOnLongClickListener {
            if (!isSelectModeEnabled) {
                val activity = (context as MainActivity)
                activity.toolbarDeleteLayout.visibility = View.VISIBLE
                activity.toolbarDeleteLayout.toolbarDelete_itemSelected.text = "1 item selected"
                activity.tabLayout.visibility = View.GONE
                isSelectModeEnabled = true
                holder.selectCheckBox.isChecked = true
                selectedTodoLists.add(todoLists[position])
                notifyDataSetChanged()
            } else {
                holder.selectCheckBox.isChecked = !holder.selectCheckBox.isChecked
                if (holder.selectCheckBox.isChecked) {
                    selectedTodoLists.add(todoLists[position])
                    var number =
                        activity.toolbarDeleteLayout.toolbarDelete_itemSelected.text.split(
                            " "
                        )[0].toInt()
                    number++
                    activity.toolbarDeleteLayout.toolbarDelete_itemSelected.text = "$number item selected"
                } else {
                    selectedTodoLists.remove(todoLists[position])
                    var number =
                        activity.toolbarDeleteLayout.toolbarDelete_itemSelected.text.split(
                            " "
                        )[0].toInt()
                    number--
                    activity.toolbarDeleteLayout.toolbarDelete_itemSelected.text = "$number item selected"
                }
            }
            true
        }

        holder.selectCheckBox.setOnClickListener {
            if(isSelectModeEnabled){
                if (holder.selectCheckBox.isChecked) {
                    var number =
                        activity.toolbarDeleteLayout.toolbarDelete_itemSelected.text.split(
                            " "
                        )[0].toInt()
                    number++

                    activity.toolbarDeleteLayout.toolbarDelete_itemSelected.text = "$number item selected"
                    selectedTodoLists.add(todoLists[position])
                }
                else {
                    selectedTodoLists.remove(todoLists[position])
                    var number =
                        activity.toolbarDeleteLayout.toolbarDelete_itemSelected.text.split(
                            " "
                        )[0].toInt()
                    number--
                    activity.toolbarDeleteLayout.toolbarDelete_itemSelected.text = "$number item selected"
                }
            }
        }
    }

    override fun getItemCount(): Int = todoLists.size

    fun getSelectedTodoLists(): MutableList<TodoListWithTasks> = selectedTodoLists













    inner class TasksAdapter(
        var adapterRef: TodoListAdapter,
        var parentViewHolder: TodoListViewHolder,
        var tasksList: List<Task>,
        private val viewModel: TodoFragmentViewModel
    ) :
        RecyclerView.Adapter<TasksAdapter.TaskViewHolder>() {
        inner class TaskViewHolder(view: View) : ViewHolder(view) {
            var taskBody = view.todo_body
            var taskChecked = view.todo_check
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.todo_item,
                parent,
                false
            )
            return TaskViewHolder(view)
        }

        override fun getItemCount(): Int = tasksList.size

        override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
            holder.taskBody.text = tasksList[position].body
            holder.taskChecked.isChecked = tasksList[position].checked
            if (holder.taskChecked.isChecked)
                holder.taskBody.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            else
                holder.taskBody.paintFlags = 0

            holder.taskChecked.setOnClickListener {
                val currentTask = tasksList[position]
                CoroutineScope(Dispatchers.IO).launch {
                    currentTask.checked = !currentTask.checked
                    viewModel.updateTask(currentTask)
                    withContext(Dispatchers.Main) {
                        holder.taskBody.paintFlags =
                            if (holder.taskChecked.isChecked) Paint.STRIKE_THRU_TEXT_FLAG else 0
                    }
                }
            }

            holder.itemView.setOnLongClickListener {
                var activity = (context as MainActivity)
                activity.toolbarDeleteLayout.visibility = View.VISIBLE
                activity.tabLayout.visibility = View.GONE
                isSelectModeEnabled = true
                // check the todoList that holds this task
                parentViewHolder.selectCheckBox.isChecked = true
                adapterRef.notifyDataSetChanged()
                true
            }


        }
    }

}

