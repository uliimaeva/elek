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
import pp.dair.models.Note
import pp.dair.models.NotePatch
import pp.dair.retrofit.Common
import pp.dair.viewmodels.NoteViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.format.DateTimeFormatter
import java.util.Date

class NoteNDialogFragment : DialogFragment() {

    var onCloseHook: (() -> Unit)? = null
    var noteId: Int? = null;
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

            val formatter = DateTimeFormatter.ofPattern("dd.mm.yyyy")
            val date = Common.currentNote!!.date
            noteName.setText(Common.currentNote!!.title, TextView.BufferType.EDITABLE)
            noteData.text = String.format("%02d.%02d.%d", date.date, date.month + 1, date.year - 100)
            noteSub.text = Common.currentNote!!.subject
            noteText.editText!!.setText(Common.currentNote!!.text, TextView.BufferType.EDITABLE)
            noteId = Common.currentNote!!.id

            noteText.counterMaxLength = 500

            addNote.setOnClickListener{
                validation()

            }
            delNote.setOnClickListener{
                noteDelete()
                dialog?.cancel()
                onCloseHook?.invoke()
                Toast.makeText(activity, "Заметка успешно удалилась!", Toast.LENGTH_SHORT).show()
            }


            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun validation() {
        if (noteName.text.isEmpty()){
            Toast.makeText(activity, "Имя не может быть путым!", Toast.LENGTH_SHORT).show()
        } else if (noteEditText.text!!.isEmpty()){
            Toast.makeText(activity, "Текст не может быть пустым!", Toast.LENGTH_SHORT).show()
        } else {
            noteEdit()
            dialog?.cancel()
            onCloseHook?.invoke()
            Toast.makeText(activity, "Заметка успешно отредактировалась!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun noteDelete() {
        val noteViewModel: NoteViewModel = NoteViewModel()
        noteViewModel.deleteNote(
            noteId!!
        , callback = object : Callback<Response<Void>> {
            override fun onResponse(call: Call<Response<Void>>, response: Response<Response<Void>>) {
                if (response.isSuccessful) {
                    Log.d("Deleted", "Cool!")
                    noteId = null;
                }
            }

            override fun onFailure(call: Call<Response<Void>>, t: Throwable) {
                Log.d("sad((", t.toString())
            }
        })
    }

    private fun noteEdit() {
        val noteViewModel: NoteViewModel = NoteViewModel()
        noteViewModel.patchNote(noteId!!, NotePatch(noteEditText.text.toString(), noteName.text.toString()), callback = object : Callback<Note> {
            override fun onResponse(call: Call<Note>, response: Response<Note>) {
                if (response.isSuccessful) {
                    Log.d("Patched", "Cool!")
                }
            }

            override fun onFailure(call: Call<Note>, t: Throwable) {
                Log.d("sad((", t.toString())
            }

        })
    }
}
