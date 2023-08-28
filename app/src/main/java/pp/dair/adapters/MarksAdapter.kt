package pp.dair.adapters

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import pp.dair.R
import pp.dair.models.JournalMark
import kotlin.math.roundToInt

class MarksAdapter(
    private var marksArray: ArrayList<JournalMark>
): RecyclerView.Adapter<MarksAdapter.MyViewHolder>() {
    private var grouppedData: ArrayList<Pair<String, ArrayList<JournalMark>>> = ArrayList()

    class MyViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val marks: TextView = itemView.findViewById(R.id.text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.journal_recycler, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MarksAdapter.MyViewHolder, position: Int) {
        holder.marks.text = getRow(grouppedData[position].second)
    }

    override fun getItemCount(): Int {
        return grouppedData.size
    }

    fun getRow(array: ArrayList<JournalMark>): String {
        var res = ""
        for (el in array.filter { it.mark.length > 0 }) {
            res += el.mark + " "
        }
        return res
    }

    fun setArray(array: ArrayList<JournalMark>) {
        this.marksArray = array
        groupMarks()
        notifyDataSetChanged()
    }

    fun groupMarks() {
        val subjects: Set<String> = marksArray.map { it.subject }.toSet()
        val res: ArrayList<Pair<String, ArrayList<JournalMark>>> = ArrayList()
        for (subject in subjects) {
            val filtered_marks = marksArray.filter { it.subject == subject }
            res.add(Pair(subject, ArrayList(filtered_marks.toList())))
        }
        grouppedData = res
    }
}