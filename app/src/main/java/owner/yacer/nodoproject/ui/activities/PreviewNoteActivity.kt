package owner.yacer.nodoproject.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_preview_note.*
import kotlinx.android.synthetic.main.pick_color_layout.*
import kotlinx.android.synthetic.main.pick_color_layout.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import owner.yacer.nodoproject.R
import owner.yacer.nodoproject.data.models.Note
import owner.yacer.nodoproject.domain.di.DaggerMyComponent
import owner.yacer.nodoproject.ui.adapters.NoteImagesAdapter
import owner.yacer.nodoproject.ui.viewmodels.PreviewViewModel
import java.text.SimpleDateFormat
import java.util.*

class PreviewNoteActivity : AppCompatActivity() {
    lateinit var note: Note
    lateinit var previewViewModel: PreviewViewModel
    lateinit var bottomSheetBehavior:BottomSheetBehavior<LinearLayout>
    private lateinit var noteImagesAdapter: NoteImagesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_note)
        val component = DaggerMyComponent.create()
        previewViewModel = component.getPreviewViewModel()

        noteImagesAdapter = NoteImagesAdapter(this)
        previewNote_rv_noteImages.adapter = noteImagesAdapter
        previewNote_rv_noteImages.layoutManager = LinearLayoutManager(this)

        getDataFromIntent()
        setNoteToViews()
        initBottomColors()

        previewNote_btn_back.setOnClickListener {
            finish()
        }

        previewNote_btn_changeColor.setOnClickListener {
            if(bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED){
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }else{
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        previewNote_btn_export.setOnClickListener {
            startActivity(createShareIntent())
        }

        previewNote_btn_save.setOnClickListener {
            val title = previewNote_et_noteTitle.text.toString()
            val body = previewNote_et_noteBody.text.toString()
            val updatedNote = Note(title, body = body, note.time, noteId = note.noteId)
            lifecycleScope.launch (Dispatchers.IO){
                previewViewModel.updateNote(updatedNote)
                withContext(Dispatchers.Main){
                    finish()
                }
            }
        }


    }

    private fun getDataFromIntent() {
        val id = intent.getIntExtra("noteId", 0)
        val title = intent.getStringExtra("noteTitle")
        val body = intent.getStringExtra("noteBody")
        val time = intent.getLongExtra("noteTime", 0)
        note = Note(title!!, body!!, time, noteId = id)

        // get note images
        lifecycleScope.launch(Dispatchers.IO) {
            val listOfNoteImages = previewViewModel.getNoteImages(id)
            val listOfImages = LinkedList<String>()
            listOfNoteImages.forEach { noteImage ->
                listOfImages.add(noteImage.imgUrl)
            }
            withContext(Dispatchers.Main){
                noteImagesAdapter.listOfImages = listOfImages
                noteImagesAdapter.notifyDataSetChanged()
            }
        }

    }

    private fun setNoteToViews() {
        previewNote_et_noteTitle.setText(note.title)
        previewNote_et_noteBody.setText(note.body)
        val sdf = SimpleDateFormat("EEE ,dd MMM yyyy hh:mm", Locale.getDefault())
        val formattedDate = sdf.format(Date(note.time))
        previewNote_noteTime.text = formattedDate
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(
            android.R.anim.slide_in_left, android.R.anim.slide_out_right
        )
    }
    private fun initBottomColors(){
        bottomSheetBehavior = BottomSheetBehavior.from(layout_bottomsheetColors)
        layout_bottomsheetColors.fl_whiteRadio.setOnClickListener {
            preview_layout.setBackgroundColor(resources.getColor(R.color.white))
        }
        layout_bottomsheetColors.fl_yellowRadio.setOnClickListener {
            preview_layout.setBackgroundColor(resources.getColor(R.color.yellow))

        }
        layout_bottomsheetColors.fl_greenRadio.setOnClickListener {
            preview_layout.setBackgroundColor(resources.getColor(R.color.green_r))
        }
        layout_bottomsheetColors.fl_redRadio.setOnClickListener {
            preview_layout.setBackgroundColor(resources.getColor(R.color.red_r))
        }
        layout_bottomsheetColors.fl_blackRadio.setOnClickListener {
            preview_layout.setBackgroundColor(resources.getColor(R.color.black))
        }

    }

    private fun createShareIntent(): Intent? {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "${note.title} \n\n ${note.body}")
        return shareIntent
    }
}