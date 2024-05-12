package pp.dair.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pp.dair.R
import pp.dair.activities.NoteNDialogFragment
import pp.dair.activities.NoteSDialogFragment
import pp.dair.models.JournalMark
import pp.dair.models.Note
import pp.dair.retrofit.Common
import java.util.TreeMap

class MainNoteAdapter (
    private var noteMap: Map<String, ArrayList<Note>>,
    private val activity: Activity,
    private val context: Context
): RecyclerView.Adapter<MainNoteAdapter.MyViewHolder>() {
    private var orderedMap: TreeMap<String, ArrayList<Note>> = TreeMap(noteMap)
    private var filteredOrderedMap: TreeMap<String, ArrayList<Note>> = TreeMap()

    var listener: (() -> Unit)? = null
    var pattern: String = ""

    class MyViewHolder (item: View): RecyclerView.ViewHolder(item) {
        val heading: TextView = itemView.findViewById(R.id.sub_name)
        val recyclerView: RecyclerView = item.findViewById(R.id.note_recycler)
        val mainLayout: RelativeLayout = itemView.findViewById(R.id.mainLayout)
    }

    fun filterMap() {
        if (pattern.isEmpty()) {
            filteredOrderedMap = orderedMap
            return
        }
        val tMap: MutableMap<String, ArrayList<Note>> = mutableMapOf()
        for (pair in noteMap) {
            val filteredList = pair.value.filter { it.title.contains(pattern, ignoreCase = true) || it.text.contains(pattern, ignoreCase = true) }
            if (filteredList.isNotEmpty()) {
                tMap.set(pair.key, ArrayList(filteredList))
            }
        }
        filteredOrderedMap = TreeMap(tMap)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.note_recycler, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val tpl = filteredOrderedMap.toList().get(position)
        holder.heading.text = tpl.first

        val adapter = NoteAdapter(ArrayList(tpl.second.filter { it.title.length > 0 }), activity, context)
        adapter.listener = listener
        holder.recyclerView.layoutManager = GridLayoutManager(activity, 2)
        holder.recyclerView.adapter = adapter
    }

    override fun getItemCount(): Int {
        return filteredOrderedMap.size
    }

    fun setArray(map: Map<String, ArrayList<Note>>) {
        this.noteMap = map
        this.orderedMap = TreeMap(map)
        filterMap()
        notifyDataSetChanged()
    }
}