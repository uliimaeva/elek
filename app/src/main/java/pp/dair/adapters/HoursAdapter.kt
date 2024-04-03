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

class HoursAdapter(
    private var hoursMap: Map<String, ArrayList<TeacherLoad>>,
    private val activity: Activity,
    private val context: Context,
): RecyclerView.Adapter<HoursAdapter.MyVewHolder>() {

    class MyVewHolder(item: View): RecyclerView.ViewHolder(item) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.hours_recycler)
        return MyVewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HoursAdapter.MyVewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return
    }

}