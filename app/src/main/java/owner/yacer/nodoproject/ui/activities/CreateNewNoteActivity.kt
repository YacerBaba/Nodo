package owner.yacer.nodoproject.ui.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.FishBun.Companion.INTENT_PATH
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import kotlinx.android.synthetic.main.activity_create_new_note.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import owner.yacer.nodoproject.R
import owner.yacer.nodoproject.data.models.Note
import owner.yacer.nodoproject.data.models.NoteImage
import owner.yacer.nodoproject.domain.di.DaggerMyComponent
import owner.yacer.nodoproject.ui.adapters.NoteImagesAdapter
import owner.yacer.nodoproject.ui.viewmodels.CreateNewNoteViewModel
import java.security.Permission
import java.util.*

private const val REQUEST_CODE = 101

class CreateNewNoteActivity : AppCompatActivity() {

    lateinit var newNoteViewModel: CreateNewNoteViewModel
    lateinit var noteImagesAdapter: NoteImagesAdapter
    var imgList = LinkedList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_note)

        val component = DaggerMyComponent.create()
        newNoteViewModel = component.getCreateNoteViewModel()

        noteImagesAdapter = NoteImagesAdapter(this)
        newNote_rv_noteImages.adapter = noteImagesAdapter
        newNote_rv_noteImages.layoutManager = LinearLayoutManager(this)

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
                val addedNoteId = newNoteViewModel.addNote(note)
                if(imgList.isNotEmpty()){
                    imgList.forEach { uri ->
                        val noteImage = NoteImage(addedNoteId, uri)
                        newNoteViewModel.addNoteImage(noteImage)
                    }
                }
                finish()
            }
        }

        newNote_btn_addImg.setOnClickListener {
            if (hasReadExternalStoragePermission())
                openGallery()
            else
                requestPermission()
        }

    }

    override fun finish() {
        super.finish()
        overridePendingTransition(
            android.R.anim.slide_in_left, android.R.anim.slide_out_right
        )
    }

    private fun openGallery() {
        FishBun.with(this).setImageAdapter(GlideAdapter()).startAlbumWithOnActivityResult(1)
    }

    private fun hasReadExternalStoragePermission(): Boolean =
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE), 1
        )
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val paths = data?.getParcelableArrayListExtra<Parcelable>(INTENT_PATH);
            paths!!.forEach { path ->
                imgList.add(path.toString())
            }
            Log.e("msg",imgList.toString())
            noteImagesAdapter.listOfImages = imgList
            noteImagesAdapter.notifyDataSetChanged()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            openGallery()
        }
    }
}