package owner.yacer.nodoproject.app

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_tab_layout.view.*
import kotlinx.android.synthetic.main.toolbar_delete.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import owner.yacer.nodoproject.R
import owner.yacer.nodoproject.data.repository.LocalRepositoryImpl
import owner.yacer.nodoproject.ui.adapters.FragmentAdapter
import owner.yacer.nodoproject.ui.fragments.NotesFragment
import owner.yacer.nodoproject.ui.fragments.TodoFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // initialize the repository with context
        LocalRepositoryImpl.initDatabase(this)
        val fragmentAdapter = FragmentAdapter(supportFragmentManager, lifecycle)
        appViewPager.adapter = fragmentAdapter
        setTabsLogic()
        toolbar_delete.setOnClickListener {
            val currentFragment = fragmentAdapter.getFragmentByPosition(appViewPager.currentItem)
            if (currentFragment is NotesFragment) {
                val selectedNotes = currentFragment.notesAdapter.getSelectedNotes()
                lifecycleScope.launch {
                    CoroutineScope(Dispatchers.IO).launch{
                        for (note in selectedNotes) {
                            LocalRepositoryImpl.deleteNote(note.note)
                        }
                    }.join()
                    currentFragment.notesAdapter.notesList.removeAll(selectedNotes)
                    currentFragment.notesAdapter.isSelectionModeEnable = false
                    currentFragment.notesAdapter.notifyDataSetChanged()
                    toolbarDeleteLayout.visibility = View.GONE
                    tabLayout.visibility = View.VISIBLE
                }
            } else {
                lifecycleScope.launch{
                    val selectedTodoLists =
                        (currentFragment as TodoFragment).adapter.getSelectedTodoLists()
                    CoroutineScope(Dispatchers.IO).launch {
                        for(todoList in selectedTodoLists){
                            // delete tasks of todoList
                            for(task in todoList.tasks){
                                LocalRepositoryImpl.deleteTask(task)
                            }
                            LocalRepositoryImpl.deleteTodoList(todoList.todoList)
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
                val currentFragment = fragmentAdapter.getFragmentByPosition(appViewPager.currentItem)
                if(currentFragment is NotesFragment){
                    currentFragment.notesAdapter.isSelectionModeEnable = false
                    currentFragment.notesAdapter.notifyDataSetChanged()
                }else{
                    (currentFragment as TodoFragment).adapter.isSelectModeEnabled = false
                    currentFragment.adapter.notifyDataSetChanged()
                }
            }catch (_:java.lang.Exception){

            }
            toolbarDeleteLayout.visibility = View.GONE
            tabLayout.visibility = View.VISIBLE
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
                    iconBlack.setImageResource(R.drawable.note)
                    iconColored.setImageResource(R.drawable.noteyellow)
                    iconBlack.visibility = View.GONE
                    iconColored.visibility = View.VISIBLE
                }
                1 -> {
                    iconBlack.setImageResource(R.drawable.ic_todo)
                    iconColored.setImageResource(R.drawable.ic_todoyellow)
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