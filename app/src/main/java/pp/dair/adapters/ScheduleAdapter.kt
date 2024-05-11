package pp.dair.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getDrawable
import androidx.recyclerview.widget.RecyclerView
import pp.dair.R
import pp.dair.activities.ScheduleDialogFragment
import pp.dair.models.LessonWithMark
import pp.dair.retrofit.Common


class ScheduleAdapter(
    private var scheduleArray: ArrayList<LessonWithMark>,
    private var activity: Activity,
    private var context: Context
): RecyclerView.Adapter<ScheduleAdapter.MyViewHolder>() {

    private var listener: (() -> Unit)? = null
    fun setListener(listener: (() -> Unit)?) {
        this.listener = listener
    }

    class MyViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val subject: TextView = itemView.findViewById(R.id.sub_name)
        val mark: TextView = itemView.findViewById(R.id.mark)
        val marker: ImageView = itemView.findViewById(R.id.visit)
        val mainLayout: RelativeLayout = item.findViewById(R.id.mainLayout)
        val audName: TextView = item.findViewById(R.id.aud)
        val pre: TextView = itemView.findViewById(R.id.pre)
        val time: TextView = itemView.findViewById(R.id.time)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.shedule_recycler, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var subject_header = scheduleArray[position].number.toString() + ") " + scheduleArray[position].subject
        if (scheduleArray[position].subgroup != 0) {
            subject_header += String.format(" (%d п/г)", scheduleArray[position].subgroup)
        }
        holder.subject.text = subject_header
        holder.audName.text = scheduleArray[position].audience
        holder.pre.text = scheduleArray[position].teacher
        holder.time.text = scheduleArray[position].time
        holder.mark.text = ""
        if (scheduleArray[position].mark != null) {
            holder.mark.text = scheduleArray[position].mark!!.mark
            if (scheduleArray[position].mark!!.closed) {
                holder.marker.setImageDrawable(getDrawable(activity, R.drawable.visit_line_blue))
            } else if (scheduleArray[position].mark!!.late) {
                holder.marker.setImageDrawable(getDrawable(activity, R.drawable.visit_line_red))
            } else if (scheduleArray[position].mark!!.missing) {
                holder.marker.setImageDrawable(getDrawable(activity, R.drawable.visit_line_red))
            }
        }

        holder.mainLayout.setOnClickListener(View.OnClickListener {
            val scheduleDialogFragment = ScheduleDialogFragment()
            val manager = (context as AppCompatActivity).supportFragmentManager;
            scheduleDialogFragment.show(manager, "scheduleDialog")
            Common.currentSub = scheduleArray[position]
            listener?.invoke()
        })
    }


    override fun getItemCount(): Int {
        return scheduleArray.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setArray(array: ArrayList<LessonWithMark>) {
        this.scheduleArray = array
        notifyDataSetChanged()
    }
}