package pp.dair.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pp.dair.R
import pp.dair.models.Note
import pp.dair.models.TeacherNote
import java.util.TreeMap

class TeacherGroupedNoteAdapter(
    private var noteList: ArrayList<TeacherNote>,
    private val activity: Activity,
    private val context: Context,
    private val groupLambda: (_: ArrayList<TeacherNote>) -> TreeMap<String, List<TeacherNote>>
): RecyclerView.Adapter<TeacherGroupedNoteAdapter.MyViewHolder>() {
    private var orderedMap: TreeMap<String, List<TeacherNote>> = TreeMap(groupLambda(noteList))

    var onOpenCallback: ((TeacherNote) -> Unit)? = null

    class MyViewHolder (item: View): RecyclerView.ViewHolder(item) {
        val heading: TextView = itemView.findViewById(R.id.sub_name)
        val recyclerView: RecyclerView = item.findViewById(R.id.note_recycler)
        val mainLayout: RelativeLayout = itemView.findViewById(R.id.mainLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.note_recycler, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val tpl = orderedMap.toList().get(position)
        holder.heading.text = tpl.first

        val adapter = TeacherNoteAdapter(ArrayList(tpl.second.filter { it.title.length > 0 }), activity, context)
        adapter.onOpenCallback = onOpenCallback
        holder.recyclerView.layoutManager = GridLayoutManager(activity, 2)
        holder.recyclerView.adapter = adapter
    }

    override fun getItemCount(): Int {
        return orderedMap.size
    }

    fun setArray(notes: ArrayList<TeacherNote>) {
        this.noteList = notes
        this.orderedMap = groupLambda(notes)
        notifyDataSetChanged()
    }
}