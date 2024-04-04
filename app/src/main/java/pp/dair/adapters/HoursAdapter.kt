package pp.dair.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pp.dair.R
import pp.dair.models.TeacherLoad
import pp.dair.models.TeacherLoadResponse
import kotlin.time.Duration.Companion.seconds

class HoursAdapter(
    private var totalMap: Map<String, TeacherLoad>,
    private var leftMap: Map<String, String>,
    private val activity: Activity,
    private val context: Context,
): RecyclerView.Adapter<HoursAdapter.MyVewHolder>() {

    class MyVewHolder(item: View): RecyclerView.ViewHolder(item) {
        val group: TextView = itemView.findViewById(R.id.group)
        val subject: TextView = itemView.findViewById(R.id.subject)
        val readHours: TextView = itemView.findViewById(R.id.read_hours)
        val remainHours: TextView = itemView.findViewById(R.id.remaining_hours)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.hours_recycler, parent, false)
        return MyVewHolder(itemView)
    }

    private fun processTotal(line: String): Int? {
        val num = line.split("/")[0]
        return num.toIntOrNull()
    }

    override fun onBindViewHolder(holder: HoursAdapter.MyVewHolder, position: Int) {
        if (position != totalMap.size) {
            val mapList = totalMap.toList()
            holder.group.text = mapList[position].second.group
            holder.subject.text = mapList[position].second.subject
            holder.readHours.text = mapList[position].second.hours.toString()
            holder.remainHours.text = "-" // потом обновится, если данные в апи есть
            holder.remainHours.text = leftMap[mapList[position].second.group]?.split("/")?.get(0)
        } else {
            holder.group.text = ""
            holder.subject.text = "Итого"
            holder.readHours.text = totalMap.toList().map { it.second.hours }.sumOf { it ?: 0 }.toString()
            holder.remainHours.text = leftMap.toList().map { processTotal(it.second) }.sumOf { it ?: 0 }.toString()
        }
    }

    override fun getItemCount(): Int {
        return totalMap.size + 1
    }

    fun setData(res: TeacherLoadResponse) {
        totalMap = res.total
        leftMap = res.left
        notifyDataSetChanged()
    }
}