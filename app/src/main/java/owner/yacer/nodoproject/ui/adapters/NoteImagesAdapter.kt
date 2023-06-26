package owner.yacer.nodoproject.ui.adapters
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.note_img_item.view.*
import owner.yacer.nodoproject.R

class NoteImagesAdapter (val context: Context) : RecyclerView.Adapter<NoteImagesAdapter.ViewHolder>() {
    var listOfImages:List<String> = listOf()
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val noteImageView = view.iv_noteImg
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.note_img_item,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listOfImages.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.noteImageView.setImageURI(Uri.parse(listOfImages[position]))
    }
}