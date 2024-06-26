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
import pp.dair.activities.NoteTeacherCreateFragment
import pp.dair.activities.ScheduleDialogFragment
import pp.dair.models.LessonWithGroup
import pp.dair.models.LessonWithMark
import pp.dair.models.Staff
import pp.dair.retrofit.Common


class ScheduleTeacherAdapter(
    private var scheduleArray: ArrayList<LessonWithGroup>,
    private var activity: Activity,
    private var context: Context
): RecyclerView.Adapter<ScheduleTeacherAdapter.MyViewHolder>() {

    private var listener: (() -> Unit)? = null
    fun setListener(listener: (() -> Unit)?) {
        this.listener = listener
    }

    class MyViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val subject: TextView = itemView.findViewById(R.id.sub_name)
        val group: TextView = itemView.findViewById(R.id.group)
        val aud: TextView = itemView.findViewById(R.id.aud)
        val time: TextView = itemView.findViewById(R.id.time)
        val mainLayout: RelativeLayout = item.findViewById(R.id.mainLayout)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.shedule_recycler_teacher, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var subject_header = scheduleArray[position].number.toString() + ") " + scheduleArray[position].subject
        if (scheduleArray[position].subgroup != 0) {
            subject_header += String.format(" (%d п/г)", scheduleArray[position].subgroup)
        }
        holder.subject.text = subject_header
        holder.aud.text = scheduleArray[position].audience
        holder.group.text = scheduleArray[position].group
        holder.time.text = scheduleArray[position].time

//
        holder.mainLayout.setOnClickListener(View.OnClickListener {
            val scheduleDialogFragment = NoteTeacherCreateFragment(scheduleArray[position])
            val manager = (context as AppCompatActivity).supportFragmentManager;
            scheduleDialogFragment.show(manager, "scheduleDialog")
        })
    }


    override fun getItemCount(): Int {
        return scheduleArray.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setArray(array: ArrayList<LessonWithGroup>) {
        this.scheduleArray = array
        notifyDataSetChanged()
    }
}