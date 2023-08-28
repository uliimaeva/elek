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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.withContext
import pp.dair.R
import pp.dair.activities.JournalActivity
import pp.dair.models.JournalMark
import kotlin.math.roundToInt

class JournalAdapter(
    private var marksArray: ArrayList<JournalMark>,
    private val activity: Activity
): RecyclerView.Adapter<JournalAdapter.MyViewHolder>() {
    private var grouppedData: ArrayList<Pair<String, ArrayList<JournalMark>>> = ArrayList()

    class MyViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val subject: TextView = itemView.findViewById(R.id.sub_name)
        val marks: RecyclerView = itemView.findViewById(R.id.recyclerViewMarks)
        val main_mark: TextView = itemView.findViewById(R.id.main_mark)
        val average: TextView = itemView.findViewById(R.id.abc_mark)
        val arrowButton1: ImageButton = itemView.findViewById(R.id.arrowButton1)
        val arrowButton2: ImageButton = itemView.findViewById(R.id.arrowButton2)
        val scrollView: NestedScrollView = itemView.findViewById(R.id.scrollView)


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
//        holder.marks.text = getRow(grouppedData[position].second)

        holder.arrowButton2.setColorFilter(Color.GRAY)

        holder.arrowButton1.setOnClickListener{
            holder.arrowButton2.isClickable = true
            holder.arrowButton1.isClickable = false
            holder.scrollView.visibility = View.VISIBLE

            holder.arrowButton2.setColorFilter(Color.parseColor("#1C2E45"))
            holder.arrowButton1.setColorFilter(Color.GRAY)
        }
        holder.arrowButton2.setOnClickListener{
            holder.arrowButton2.isClickable = false
            holder.arrowButton1.isClickable = true
            holder.scrollView.visibility = View.GONE

            holder.arrowButton1.setColorFilter(Color.parseColor("#1C2E45"))
            holder.arrowButton2.setColorFilter(Color.GRAY)
        }

        val adapter = ScheduleAdapter(ArrayList(), activity)
        holder.marks.layoutManager = GridLayoutManager(activity, 2)
        holder.marks.adapter = adapter


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