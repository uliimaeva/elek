package pp.dair.adapters

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import pp.dair.R
import pp.dair.models.JournalMark
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class MarksAdapter(
    private var marksArray: ArrayList<JournalMark>,
    private val activity: Activity
): RecyclerView.Adapter<MarksAdapter.MyViewHolder>() {
    class MyViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val marks: TextView = itemView.findViewById(R.id.markText)
        val date: TextView = itemView.findViewById(R.id.dateText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.marks_recycler_view, parent, false)
        return MyViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MarksAdapter.MyViewHolder, position: Int) {

        holder.marks.text = String.format("%s", marksArray[position].mark)
        val date = marksArray[position].date

        val formatter = DateTimeFormatter.ofPattern("dd.mm.yy")
        val d = String.format("%02d.%02d.%d", date.date, date.month + 1, date.year - 100)
        holder.date.text = d
    }

    override fun getItemCount(): Int {
        return marksArray.size
    }

    fun setArray(array: ArrayList<JournalMark>) {
        this.marksArray = array
        notifyDataSetChanged()
    }
}

