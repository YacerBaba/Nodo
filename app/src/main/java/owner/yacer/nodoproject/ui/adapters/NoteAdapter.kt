package owner.yacer.nodoproject.ui.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.note_item.view.*
import kotlinx.android.synthetic.main.toolbar_delete.view.*
import owner.yacer.nodoproject.R
import owner.yacer.nodoproject.app.MainActivity
import owner.yacer.nodoproject.data.models.Note
import owner.yacer.nodoproject.data.models.NoteWithNoteImages
import owner.yacer.nodoproject.data.repository.LocalRepositoryImpl
import owner.yacer.nodoproject.ui.activities.PreviewNoteActivity
import java.text.SimpleDateFormat
import java.util.*

class NoteAdapter(val context: Context) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    var notesList: MutableList<NoteWithNoteImages> = mutableListOf()
    private var selectedNotes = mutableListOf<NoteWithNoteImages>()
    var isSelectionModeEnable = false
    private var activity = (context as MainActivity)

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.note_title
        val body = view.note_body
        val time = view.note_time
        val selectCheckBox = view.note_selected
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteAdapter.NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.note_item,
            parent,
            false
        )
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteAdapter.NoteViewHolder, position: Int) {
        holder.title.text = notesList[position].note.title
        holder.body.text = notesList[position].note.body
        val bgColor = notesList[position].note.bgColor
        if(bgColor != null)
            holder.itemView.setBackgroundColor(bgColor)
        else
            holder.itemView.setBackgroundColor(Color.WHITE)
        if (isSelectionModeEnable) {
            holder.selectCheckBox.visibility = View.VISIBLE
            activity.appViewPager.isUserInputEnabled = false
        } else {
            holder.selectCheckBox.isChecked = false
            holder.selectCheckBox.visibility = View.GONE
            activity.appViewPager.isUserInputEnabled = true
        }



        val sdf = SimpleDateFormat("EEE ,dd MMM yyyy hh:mm", Locale.getDefault())
        val currentDate = sdf.format(Date(notesList[position].note.time))
        holder.time.text = currentDate
        holder.itemView.setOnClickListener {
            Intent(context, PreviewNoteActivity::class.java).also {
                it.putExtra("noteId", notesList[position].note.noteId)
                it.putExtra("noteTitle", notesList[position].note.title)
                it.putExtra("noteBody", notesList[position].note.body)
                it.putExtra("noteTime", notesList[position].note.time)
                it.putExtra("bgColor",notesList[position].note.bgColor)
                context.startActivity(it)
                (context as Activity).overridePendingTransition(
                    R.anim.slide_in_right, R.anim.slide_out_left
                )
            }
        }

        holder.itemView.setOnLongClickListener {
            if (!isSelectionModeEnable) {
                val activity = (context as MainActivity)
                activity.toolbarDeleteLayout.visibility = View.VISIBLE
                activity.tabLayout.visibility = View.GONE
                isSelectionModeEnable = true
                holder.selectCheckBox.isChecked = true
                activity.toolbarDeleteLayout.toolbarDelete_itemSelected.text = "1 item selected"
                selectedNotes.add(notesList[position])
                notifyDataSetChanged()
            } else {
                holder.selectCheckBox.isChecked = !holder.selectCheckBox.isChecked
                if (holder.selectCheckBox.isChecked) {
                    selectedNotes.add(notesList[position])
                    var number =
                        activity.toolbarDeleteLayout.toolbarDelete_itemSelected.text.split(
                            " "
                        )[0].toInt()
                    number++
                    activity.toolbarDeleteLayout.toolbarDelete_itemSelected.text = "$number item selected"
                } else {
                    selectedNotes.remove(notesList[position])
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

        holder.selectCheckBox.setOnClickListener{
            if(isSelectionModeEnable){
                if(holder.selectCheckBox.isChecked){
                    selectedNotes.add(notesList[position])
                    var number =
                        activity.toolbarDeleteLayout.toolbarDelete_itemSelected.text.split(
                            " "
                        )[0].toInt()
                    number++
                    activity.toolbarDeleteLayout.toolbarDelete_itemSelected.text = "$number item selected"
                }else{
                    selectedNotes.remove(notesList[position])
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

    override fun getItemCount(): Int = notesList.size

    fun getSelectedNotes(): MutableList<NoteWithNoteImages> = selectedNotes

}