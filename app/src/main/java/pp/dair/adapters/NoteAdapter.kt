package pp.dair.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import pp.dair.R
import pp.dair.models.Note

class NoteAdapter (
    private var noteArray: ArrayList<Note>,
    private val activity: Activity
): RecyclerView.Adapter<NoteAdapter.MyViewHolder>() {

    private var listener: (() -> Unit)? = null

    fun setLisnener(listener: (() -> Unit)){
        this.listener = listener
    }
    class MyViewHolder (item: View): RecyclerView.ViewHolder(item) {
        val heading: TextView = itemView.findViewById(R.id.noteName)
        val note: TextView = itemView.findViewById(R.id.noteText)
        val mainLayout: RelativeLayout = itemView.findViewById(R.id.mainLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.note_recycler, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.heading.text = String.format("%s", noteArray[position].name)
        holder.note.text = String.format("%s", noteArray[position].noteText)

        holder.mainLayout.setOnClickListener(View.OnClickListener {
            Toast.makeText(activity, noteArray[position].name, Toast.LENGTH_SHORT).show()
            listener?.invoke()
        })
    }

    override fun getItemCount(): Int {
        return noteArray.size
    }
}