package pp.dair.activities

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import pp.dair.R
import pp.dair.retrofit.Common
import kotlin.io.path.fileVisitor

class ScheduleDialogFragment : DialogFragment() {

    lateinit var subName: TextView
    lateinit var preName: TextView
    lateinit var audName: TextView
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater.inflate(R.layout.schedule_dialog_fragment, null);

            builder.setView(inflater)

            subName = inflater.findViewById(R.id.subName)
            preName = inflater.findViewById(R.id.preName)
            audName = inflater.findViewById(R.id.audName)

            subName.text = String.format("%s" , Common.currentSub!!.subject)
            preName.text = String.format("%s", Common.currentSub!!.teacher)
            audName.text = String.format("%s", Common.currentSub!!.audience)





            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
