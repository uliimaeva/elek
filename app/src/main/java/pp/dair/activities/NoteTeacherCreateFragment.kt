package pp.dair.activities

import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
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
    lateinit var noteGru: TextView
    lateinit var sOne: RadioButton
    lateinit var sGru: RadioButton
    lateinit var noteEditText: TextInputEditText
    lateinit var noteText: TextInputLayout
    lateinit var addNote: Button
    lateinit var delNote: Button

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater.inflate(R.layout.note_t_dialog_fragment, null);

            builder.setView(inflater)

            noteName = inflater.findViewById(R.id.noteName)
            noteData = inflater.findViewById(R.id.noteDate)
            noteSub = inflater.findViewById(R.id.noteSub)
            noteEditText = inflater.findViewById(R.id.note_TIET)
            noteText = inflater.findViewById(R.id.note_TIL)
            noteGru = inflater.findViewById(R.id.noteGru)
            sOne = inflater.findViewById(R.id.s_one)
            sGru = inflater.findViewById(R.id.s_group)
            addNote = inflater.findViewById(R.id.addButton)
            delNote = inflater.findViewById(R.id.delButton)


            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            noteData.text = LocalDateTime.now().format(formatter)
            noteSub.text = data.subject
            noteGru.text = data.group

            addNote.setOnClickListener{
                validation()
            }
            delNote.visibility = View.GONE


            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun validation() {
        if (noteName.text.isEmpty()){
            Toast.makeText(activity, "Имя не может быть путым!", Toast.LENGTH_SHORT).show()
        } else if (noteEditText.text!!.isEmpty()) {
            Toast.makeText(activity, "Текст не может быть пустым!", Toast.LENGTH_SHORT).show()
        } else {
            noteAdd()
            dialog?.cancel()
            Toast.makeText(activity, "Заметка успешно создалась!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun noteAdd() {
        var pub = true
        pub = !sOne.isChecked

        val noteViewModel: NoteViewModel = NoteViewModel()
        noteViewModel.createTeacherNote(
            TeacherNoteCreate(
                noteName.text.toString(),
                noteEditText.text.toString(),
                data.subject,
                data.group,
                pub
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