package owner.yacer.nodoproject.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import owner.yacer.nodoproject.ui.fragments.NotesFragment
import owner.yacer.nodoproject.ui.fragments.TodoFragment

class FragmentAdapter(fm: FragmentManager, val lifecycle: Lifecycle) :
    FragmentStateAdapter(fm, lifecycle) {

    private var fragmentMap = HashMap<Int, Fragment>()
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val fragment = NotesFragment()
                fragmentMap[position] = fragment
                fragment
            }
            1 -> {
                val fragment = TodoFragment()
                fragmentMap[position] = fragment
                fragment

            }
            else -> NotesFragment()
        }
    }

    fun getFragmentByPosition(position: Int): Fragment? {
        return fragmentMap[position]
    }
}