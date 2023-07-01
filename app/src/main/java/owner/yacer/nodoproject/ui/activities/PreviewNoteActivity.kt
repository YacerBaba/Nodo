package owner.yacer.nodoproject.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import kotlinx.android.synthetic.main.activity_preview_note.*
import kotlinx.android.synthetic.main.pick_color_layout.*
import kotlinx.android.synthetic.main.pick_color_layout.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import owner.yacer.nodoproject.R
import owner.yacer.nodoproject.data.models.Note
import owner.yacer.nodoproject.data.models.NoteImage
import owner.yacer.nodoproject.domain.di.DaggerMyComponent
import owner.yacer.nodoproject.ui.adapters.NoteImagesAdapter
import owner.yacer.nodoproject.ui.viewmodels.PreviewViewModel
import java.text.SimpleDateFormat
import java.util.*

class PreviewNoteActivity : AppCompatActivity() {
    lateinit var note: Note
    lateinit var previewViewModel: PreviewViewModel
    lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var noteImagesAdapter: NoteImagesAdapter
    val listOfImages = LinkedList<String>()

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
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        previewNote_btn_export.setOnClickListener {
            startActivity(createShareIntent())
        }

        previewNote_btn_save.setOnClickListener {
            val title = previewNote_et_noteTitle.text.toString()
            val body = previewNote_et_noteBody.text.toString()
            val bgColor = preview_layout.background
            var color = -1
            if (bgColor is ColorDrawable) {
                color = bgColor.color
                // Use the color value as needed
            } else {
                return@setOnClickListener
            }
            val updatedNote =
                Note(title, body = body, note.time, bgColor = color, noteId = note.noteId)
            lifecycleScope.launch(Dispatchers.IO) {
                previewViewModel.updateNote(updatedNote)
                withContext(Dispatchers.Main) {
                    finish()
                }
            }
        }

        previewNote_btn_addImg.setOnClickListener {
            openGallery()
        }

        preview_layout.setOnTouchListener(object : OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (!isPointInsideView(event!!.rawX, event!!.rawY, layout_bottomsheetColors)) {
                    // Collapse the BottomNavigationView
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                    return true
                }
                return false
            }
        })

    }

    private fun isPointInsideView(rawX: Float, rawY: Float, bottomNavigationView: View): Boolean {
        val location = IntArray(2)
        bottomNavigationView.getLocationOnScreen(location)
        val viewRect = Rect(
            location[0],
            location[1],
            location[0] + bottomNavigationView.width,
            location[1] + bottomNavigationView.height
        )
        return viewRect.contains(rawX.toInt(), rawY.toInt())
    }

    private fun getDataFromIntent() {
        val id = intent.getLongExtra("noteId", 0)
        val title = intent.getStringExtra("noteTitle")
        val body = intent.getStringExtra("noteBody")
        val time = intent.getLongExtra("noteTime", 0)
        val bgColor = intent.getIntExtra("bgColor",0)
        note = Note(title!!, body!!, time, noteId = id, bgColor = bgColor)

        // get note images
        lifecycleScope.launch(Dispatchers.IO) {
            val listOfNoteImages = previewViewModel.getNoteImages(id)
            listOfNoteImages.forEach { noteImage ->
                listOfImages.add(noteImage.imgUrl)
            }
            withContext(Dispatchers.Main) {
                noteImagesAdapter.listOfImages = listOfImages
                noteImagesAdapter.notifyDataSetChanged()
            }
        }

    }

    private fun setNoteToViews() {
        previewNote_et_noteTitle.setText(note.title)
        previewNote_et_noteBody.setText(note.body)
        if(note.bgColor != null)
            preview_layout.setBackgroundColor(note.bgColor!!)
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

    private fun initBottomColors() {
        val layoutBottom = findViewById<LinearLayout>(R.id.layout_bottomsheetColors)
        bottomSheetBehavior = BottomSheetBehavior.from(layoutBottom)
        layout_bottomsheetColors.fl_white.setOnClickListener {
            defaultViewsState()
            preview_layout.setBackgroundColor(resources.getColor(R.color.white))
        }
        layout_bottomsheetColors.fl_blue.setOnClickListener {
            defaultViewsState()
            preview_layout.setBackgroundColor(resources.getColor(R.color.light_blue))
            fl_blue_iv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_done))
        }
        layout_bottomsheetColors.fl_yellow.setOnClickListener {
            defaultViewsState()
            preview_layout.setBackgroundColor(resources.getColor(R.color.light_yellow))
            fl_yellow_iv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_done))
        }
        layout_bottomsheetColors.fl_green.setOnClickListener {
            defaultViewsState()
            preview_layout.setBackgroundColor(resources.getColor(R.color.light_green))
            fl_green_iv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_done))
        }
        layout_bottomsheetColors.fl_violet.setOnClickListener {
            defaultViewsState()
            preview_layout.setBackgroundColor(resources.getColor(R.color.light_violet))
            fl_violet_iv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_done))
        }
        layout_bottomsheetColors.fl_brown.setOnClickListener {
            defaultViewsState()
            preview_layout.setBackgroundColor(resources.getColor(R.color.light_brown))
            fl_brown_iv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_done))
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        Log.e("msg", "state collapsed executed")
                        preview_layout.foreground = null
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        Log.e("msg", "state expanded executed")
                        preview_layout.foreground = ColorDrawable(Color.TRANSPARENT)

                    }
                    else -> Log.e("msg", "")
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Handle slide offset if needed
            }
        })


    }

    private fun createShareIntent(): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "${note.title} \n\n ${note.body}")
        return shareIntent
    }

    private fun defaultViewsState() {
        fl_blue_iv.setImageDrawable(null)
        fl_green_iv.setImageDrawable(null)
        fl_yellow_iv.setImageDrawable(null)
        fl_violet_iv.setImageDrawable(null)
        fl_brown_iv.setImageDrawable(null)
    }

    private fun openGallery() {
        FishBun.with(this).setImageAdapter(GlideAdapter()).startAlbumWithOnActivityResult(101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK) {
            val paths = data?.getParcelableArrayListExtra<Parcelable>(FishBun.INTENT_PATH);
            lifecycleScope.launch(Dispatchers.IO) {
                paths?.forEach { path ->
                    val noteImage = NoteImage(note.noteId, path.toString())
                    previewViewModel.addNoteImage(noteImage)
                    listOfImages.add(path.toString())
                }
                withContext(Dispatchers.Main) {
                    noteImagesAdapter.listOfImages = listOfImages
                    noteImagesAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    fun Int.toHexColor(): String {
        return String.format("#%06X", 0xFFFFFF and this)
    }
}