package pp.dair.activities

import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import pp.dair.R
import pp.dair.models.LessonWithGroup
import pp.dair.models.Note
import pp.dair.models.TeacherNote
import pp.dair.models.TeacherNoteCreate
import pp.dair.retrofit.Common
import pp.dair.viewmodels.NoteViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class NoteTeacherCreateFragment(val data: LessonWithGroup): DialogFragment() {
    lateinit var noteName: EditText
    lateinit var noteData: TextView
    lateinit var noteSub: TextView
    lateinit var noteEditText: TextInputEditText
    lateinit var noteText: TextInputLayout
    lateinit var addNote: Button
    lateinit var delNote: Button

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater.inflate(R.layout.note_dialog_fragment, null);

            builder.setView(inflater)

            noteName = inflater.findViewById(R.id.noteName)
            noteData = inflater.findViewById(R.id.noteDate)
            noteSub = inflater.findViewById(R.id.noteSub)
            noteEditText = inflater.findViewById(R.id.note_TIET)
            noteText = inflater.findViewById(R.id.note_TIL)
            addNote = inflater.findViewById(R.id.addButton)
            delNote = inflater.findViewById(R.id.delButton)

            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            noteData.text = LocalDateTime.now().format(formatter)
            noteSub.text = data.subject

            addNote.setOnClickListener{
                noteAdd()
                dialog?.cancel()
                Toast.makeText(activity, "Заметка успешно создалась!", Toast.LENGTH_SHORT).show()
            }
            delNote.setOnClickListener{
                dialog?.cancel()
                Toast.makeText(activity, "Заметка успешно удалилась!", Toast.LENGTH_SHORT).show()
            }


            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun noteAdd() {
        val noteViewModel: NoteViewModel = NoteViewModel()
        noteViewModel.createTeacherNote(
            TeacherNoteCreate(
                noteName.text.toString(),
                noteEditText.text.toString(),
                data.subject,
                data.group,
                true
            ), callback = object : Callback<TeacherNote> {
                override fun onResponse(p0: Call<TeacherNote>, p1: Response<TeacherNote>) {

                }

                override fun onFailure(p0: Call<TeacherNote>, p1: Throwable) {
                    Log.d("ERR", p1.toString())
                }
            }
        )
    }
}