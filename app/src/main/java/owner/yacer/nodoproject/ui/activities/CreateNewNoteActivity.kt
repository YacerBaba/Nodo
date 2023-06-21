package owner.yacer.nodoproject.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_create_new_note.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import owner.yacer.nodoproject.R
import owner.yacer.nodoproject.data.models.Note
import owner.yacer.nodoproject.ui.viewmodels.CreateNewNoteViewModel
import java.util.Date

class CreateNewNoteActivity : AppCompatActivity() {
    lateinit var newNoteViewModel: CreateNewNoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_note)
        newNoteViewModel = ViewModelProvider(this)[CreateNewNoteViewModel::class.java]

        newNote_btn_back.setOnClickListener {
            finish()
        }
        newNote_btn_save.setOnClickListener {
            if (newNote_et_noteBody.text.toString().isEmpty()) {
                finish()
                return@setOnClickListener
            }
            if (newNote_et_noteTitle.text.toString().isEmpty()) {

            }
            val noteTitle = newNote_et_noteTitle.text.toString()
            val noteBody = newNote_et_noteBody.text.toString()
            val currentTime = Date().time
            val note = Note(noteTitle, noteBody, currentTime)
            CoroutineScope(Dispatchers.IO).launch {
                newNoteViewModel.addNote(note)
                finish()
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(
            android.R.anim.slide_in_left, android.R.anim.slide_out_right
        )
    }

    private fun checkEditTextFocus(): Boolean {
        return newNote_et_noteTitle.hasFocus() || newNote_et_noteBody.hasFocus()
    }

    override fun onBackPressed() {

        newNote_et_noteTitle.clearFocus()
        newNote_et_noteBody.clearFocus()
        super.onBackPressed()
    }


}