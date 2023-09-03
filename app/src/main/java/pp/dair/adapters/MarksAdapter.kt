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
    private var marksArray: ArrayList<JournalMark>,
    private val activity: Activity
): RecyclerView.Adapter<MarksAdapter.MyViewHolder>() {
    class MyViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val marks: TextView = itemView.findViewById(R.id.markText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.marks_recycler_view, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MarksAdapter.MyViewHolder, position: Int) {
        holder.marks.text = String.format("%s", marksArray[position].mark)
    }

    override fun getItemCount(): Int {
        return marksArray.size
    }

    fun setArray(array: ArrayList<JournalMark>) {
        this.marksArray = array
        notifyDataSetChanged()
    }
}