package pp.dair.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import pp.dair.R
import pp.dair.activities.NoteNDialogFragment
import pp.dair.models.Note
import pp.dair.models.TeacherNote
import pp.dair.retrofit.Common

class TeacherNoteAdapter(
    private var noteArray: ArrayList<TeacherNote>,
    private val activity: Activity,
    private val context: Context
): RecyclerView.Adapter<TeacherNoteAdapter.MyViewHolder>() {
    var onOpenCallback: ((TeacherNote) -> Unit)? = null

    class MyViewHolder (item: View): RecyclerView.ViewHolder(item) {
        val heading: TextView = itemView.findViewById(R.id.noteName)
        val note: TextView = itemView.findViewById(R.id.noteText)
        val mainLayout: RelativeLayout = itemView.findViewById(R.id.mainLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.note_note_recycler, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.heading.text = String.format("%s", noteArray[position].title)
        holder.note.text = String.format("%s", noteArray[position].text)

        holder.mainLayout.setOnClickListener(View.OnClickListener {
            Common.currentTeacherNote = noteArray[position]
        })

        holder.mainLayout.setOnClickListener(View.OnClickListener {
            Common.currentTeacherNote = noteArray[position]
            onOpenCallback?.let { it1 -> it1(noteArray[position]) }
        })
    }

    override fun getItemCount(): Int {
        return noteArray.size
    }

    fun setArray(arr: ArrayList<TeacherNote>) {
        noteArray = arr
        notifyDataSetChanged()
    }
}