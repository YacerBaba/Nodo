package owner.yacer.nodoproject.app

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_tab_layout.view.*
import kotlinx.android.synthetic.main.toolbar_delete.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import owner.yacer.nodoproject.R
import owner.yacer.nodoproject.data.repository.LocalRepositoryImpl
import owner.yacer.nodoproject.domain.di.DaggerMyComponent
import owner.yacer.nodoproject.ui.adapters.FragmentAdapter
import owner.yacer.nodoproject.ui.fragments.NotesFragment
import owner.yacer.nodoproject.ui.fragments.TodoFragment
import owner.yacer.nodoproject.ui.viewmodels.MainViewModel


class MainActivity : AppCompatActivity() {
    lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = DaggerMyComponent.create().getMainViewModel()
        mainViewModel.initDatabase(this)

        val fragmentAdapter = FragmentAdapter(supportFragmentManager, lifecycle)
        appViewPager.adapter = fragmentAdapter
        setTabsLogic()

        toolbar_delete.setOnClickListener {
            val currentFragment = fragmentAdapter.getFragmentByPosition(appViewPager.currentItem)
            if (currentFragment is NotesFragment) {
                val selectedNotes = currentFragment.notesAdapter.getSelectedNotes()
                lifecycleScope.launch {
                    CoroutineScope(Dispatchers.IO).launch {

                        for (note in selectedNotes) {
                            mainViewModel.deleteNote(note.note)
                            for (noteImg in note.listOfImgs){
                                mainViewModel.deleteNoteImage(noteImg)
                            }
                        }
                    }.join()
                    currentFragment.notesAdapter.notesList.removeAll(selectedNotes)
                    currentFragment.notesAdapter.isSelectionModeEnable = false
                    currentFragment.notesAdapter.notifyDataSetChanged()
                    toolbarDeleteLayout.visibility = View.GONE
                    tabLayout.visibility = View.VISIBLE
                }
            } else if (currentFragment is TodoFragment) {
                lifecycleScope.launch {
                    val selectedTodoLists =
                        currentFragment.adapter.getSelectedTodoLists()
                    CoroutineScope(Dispatchers.IO).launch {
                        for (todoList in selectedTodoLists) {
                            // delete tasks of todoList
                            Log.e("msgSelectedTodoLists",selectedTodoLists.toString())
                            for (task in todoList.tasks) {
                                mainViewModel.deleteTask(task)
                            }
                            mainViewModel.deleteTodoList(todoList.todoList)
                        }
                    }.join()
                    currentFragment.adapter.todoLists.removeAll(selectedTodoLists)
                    currentFragment.adapter.isSelectModeEnabled = false
                    currentFragment.adapter.notifyDataSetChanged()
                    toolbarDeleteLayout.visibility = View.GONE
                    tabLayout.visibility = View.VISIBLE
                }

            }
        }

        toolbar_back.setOnClickListener {
            try {
                val currentFragment =
                    fragmentAdapter.getFragmentByPosition(appViewPager.currentItem)
                if (currentFragment is NotesFragment) {
                    currentFragment.notesAdapter.isSelectionModeEnable = false
                    currentFragment.notesAdapter.notifyDataSetChanged()
                } else if (currentFragment is TodoFragment) {
                    (currentFragment).adapter.isSelectModeEnabled = false
                    currentFragment.adapter.notifyDataSetChanged()
                }
                toolbarDeleteLayout.visibility = View.GONE
                tabLayout.visibility = View.VISIBLE
            } catch (_: java.lang.Exception) {

            }
        }

    }


    private fun setTabsLogic() {
        TabLayoutMediator(
            tabLayout, appViewPager
        ) { tab, position ->
            val customView = LayoutInflater.from(this).inflate(R.layout.custom_tab_layout, null)
            val iconBlack = customView.tab_iconBlack
            val iconColored = customView.tab_iconColored
            when (position) {
                0 -> {
                    iconBlack.setImageResource(R.drawable.note_empty)
                    iconColored.setImageResource(R.drawable.note_filled)
                    iconBlack.visibility = View.GONE
                    iconColored.visibility = View.VISIBLE
                }
                1 -> {
                    iconBlack.setImageResource(R.drawable.checkbox_empty)
                    iconColored.setImageResource(R.drawable.checkbox_filled)
                    iconBlack.visibility = View.VISIBLE
                    iconColored.visibility = View.GONE
                }
            }
            tab.customView = customView
        }.attach()

        val tabSelectedListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Get the custom view for the selected tab
                val customView = tab?.customView ?: return
                val iconBlack = customView.tab_iconBlack
                val iconColored = customView.tab_iconColored

                // Show the active icon and hide the inactive icon
                iconBlack.visibility = View.GONE
                iconColored.visibility = View.VISIBLE
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Get the custom view for the unselected tab
                val customView = tab?.customView ?: return
                val iconBlack = customView.tab_iconBlack
                val iconColored = customView.tab_iconColored

                // Show the active icon and hide the inactive icon
                iconBlack.visibility = View.VISIBLE
                iconColored.visibility = View.GONE
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        }

        // Set the listener on the TabLayout
        tabLayout.addOnTabSelectedListener(tabSelectedListener)

    }

}