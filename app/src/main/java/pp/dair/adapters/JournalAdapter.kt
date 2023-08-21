package pp.dair.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pp.dair.R
import pp.dair.models.JournalMark
import kotlin.math.roundToInt

class JournalAdapter(
    private var marksArray: ArrayList<JournalMark>
): RecyclerView.Adapter<JournalAdapter.MyViewHolder>() {
    private var grouppedData: ArrayList<Pair<String, ArrayList<JournalMark>>> = ArrayList()

    class MyViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val subject: TextView = itemView.findViewById(R.id.sub_name)
        val marks: TextView = itemView.findViewById(R.id.marks)
        val main_mark: TextView = itemView.findViewById(R.id.main_mark)
        val average: TextView = itemView.findViewById(R.id.abc_mark)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.journal_recycler, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return grouppedData.size
    }

    fun countAverage(array: ArrayList<JournalMark>): Double {
        val marks = array.filter { it.mark.toIntOrNull() != null }
        var amount = 0
        var sum = 0
        for (mark in marks) {
            amount += 1
            sum += mark.mark.toInt()
        }
        return sum.toDouble() / amount.toDouble()
    }

    fun getRow(array: ArrayList<JournalMark>): String {
        var res = ""
        for (el in array.filter { it.mark.length > 0 }) {
            res += el.mark + " "
        }
        return res
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.subject.text = grouppedData[position].first
        val avg = countAverage(grouppedData[position].second)
        if (!avg.isNaN()) {
            holder.main_mark.text = avg.roundToInt().toString()
            holder.average.text = avg.toString().take(3)
        } else {
            holder.main_mark.text = "-"
            holder.average.text = "-"
        }
        holder.marks.text = getRow(grouppedData[position].second)
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