package pp.dair.activities

import android.app.AlertDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import pp.dair.R
import pp.dair.models.Note
import pp.dair.retrofit.Common
import pp.dair.viewmodels.NoteViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

class NoteDialogFragment : DialogFragment() {

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
            noteData.text = LocalDateTime.now().format(formatter)
            noteSub.text = Common.currentSub!!.subject

            addNote.setOnClickListener{
                noteAdd()
            }
            delNote.setOnClickListener{
                noteDelete()
            }


            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
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

    private fun noteAdd() {
        if (noteId != null) { return }
        val noteViewModel: NoteViewModel = NoteViewModel()
        noteViewModel.createNote(Note(
            null,
            noteName.text.toString(),
            Date(), //noteData.text,
            noteEditText.text.toString()
        ), callback = object : Callback<Note> {
            override fun onResponse(call: Call<Note>, response: Response<Note>) {
                if (response.isSuccessful) {
                    Log.d("Created", "Cool!")
                    if (noteId == null && response.body() != null) {
                        noteId = response.body()!!.id!!
                    }
                }
            }

            override fun onFailure(call: Call<Note>, t: Throwable) {
                Log.d("sad((", t.toString())
            }
        })
    }
}
