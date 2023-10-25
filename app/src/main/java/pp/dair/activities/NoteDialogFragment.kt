package pp.dair.activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import pp.dair.R
import pp.dair.models.Note
import pp.dair.retrofit.Common
import kotlin.io.path.fileVisitor

class NoteDialogFragment : DialogFragment() {

    lateinit var noteName: TextView
    lateinit var noteData: TextView
    lateinit var noteEditText: TextInputEditText
    lateinit var noteText: TextInputLayout
    lateinit var addNote: Button
    lateinit var delNote: Button
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater.inflate(R.layout.note_dialog_fragment, null);

            builder.setView(inflater)

            noteName = inflater.findViewById(R.id.noteName)
            noteData = inflater.findViewById(R.id.noteDate)
            noteEditText = inflater.findViewById(R.id.note_TIET)
            noteText = inflater.findViewById(R.id.note_TIL)
            addNote = inflater.findViewById(R.id.addButton)
            delNote = inflater.findViewById(R.id.delButton)


            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
